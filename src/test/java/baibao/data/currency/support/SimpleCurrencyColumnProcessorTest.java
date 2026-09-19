package baibao.data.currency.support;

import kunlun.data.currency.CurrencyUtil;
import kunlun.data.currency.model.CurrencyColumn;
import kunlun.data.currency.model.CurrencyRow;
import kunlun.data.currency.support.DefaultCurrencyColumnProcessor;
import org.junit.After;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * SimpleCurrencyColumnProcessor 的纯内存测试（不依赖数据库与汇率数据源，也不涉及 Page 填充）.
 * 覆盖：字段白名单（未知/非数值跳过）、行 id 字段名自定义与缺省 id、行 id 缺失与
 * 金额为空跳过、手输汇率优先、服务端汇率解析（覆写 resolveRate）与缺省跳过、
 * 基准币种二选一（{@code baseCurrency} 直接值优先，为空按 {@code baseCurrencyField}
 * 反射读行内币种，皆空白跳过且不默认 CNY）、目标币种等于基准币种按原值展示、
 * 金额小数位与编码归一、入参配置不被回写（克隆隔离）、门面分发.
 *
 * @author Kahle
 */
public class SimpleCurrencyColumnProcessorTest {

    @After
    public void resetWiring() {
        // 恢复默认实现，避免影响其他用例
        CurrencyUtil.setCurrencyColumnProcessor(new DefaultCurrencyColumnProcessor());
    }


    // region ======== 基本换算与结果结构 ========

    @Test
    public void manualRateStructureAndCloneIsolation() {
        BillRow row = row(1L, "10000");
        CurrencyColumn config = config("billTotalAmount", "CNY",
                item("USD", "0.14"), item("EUR", "0.13"));
        List<CurrencyRow> rows = processor().process(Arrays.asList(config), "id", Arrays.asList(row));
        assertEquals(1, rows.size());
        CurrencyRow rowResult = rows.get(0);
        assertEquals("1", rowResult.getRowId());
        assertEquals(1, rowResult.getColumns().size());
        CurrencyColumn column = rowResult.getColumns().get(0);
        assertEquals("billTotalAmount", column.getAmountField());
        assertEquals("CNY", column.getBaseCurrency());
        assertEquals(2, column.getTargetAmountItems().size());
        CurrencyColumn.TargetAmountItem usd = column.getTargetAmountItems().get(0);
        assertEquals("USD", usd.getTargetCurrency());
        assertEquals(0, usd.getExchangeRate().compareTo(new BigDecimal("0.14")));
        assertEquals(0, usd.getTargetAmount().compareTo(new BigDecimal("1400.00")));
        assertEquals("1400.00", usd.getTargetAmount().toPlainString());
        assertEquals("1300.00", column.getTargetAmountItems().get(1).getTargetAmount().toPlainString());
        // 入参配置不被回写（换算结果写在克隆上）
        assertEquals("CNY", config.getBaseCurrency());
        assertNull(config.getTargetAmountItems().get(0).getTargetAmount());
    }

    @Test
    public void multipleRowsAndCurrencyColumns() {
        // 多行多金额字段：每行一个 CurrencyRow（rowId + 各字段结果），顺序同行数据
        BillRow first = row(1L, "10000");
        first.paidTotalAmount = new BigDecimal("4000");
        BillRow second = row(2L, "100");
        List<CurrencyColumn> currencyColumns = Arrays.asList(
                config("billTotalAmount", "CNY", item("USD", "0.14")),
                config("paidTotalAmount", "CNY", item("USD", "0.14")));
        List<CurrencyRow> rows = processor().process(currencyColumns, "id", Arrays.asList(first, second));
        assertEquals(2, rows.size());
        assertEquals("1", rows.get(0).getRowId());
        assertEquals(2, rows.get(0).getColumns().size());
        assertEquals(0, columnOf(rows, 1L, "billTotalAmount").getTargetAmountItems().get(0)
                .getTargetAmount().compareTo(new BigDecimal("1400.00")));
        assertEquals(0, columnOf(rows, 1L, "paidTotalAmount").getTargetAmountItems().get(0)
                .getTargetAmount().compareTo(new BigDecimal("560.00")));
        assertEquals("2", rows.get(1).getRowId());
        assertEquals(0, columnOf(rows, 2L, "billTotalAmount").getTargetAmountItems().get(0)
                .getTargetAmount().compareTo(new BigDecimal("14.00")));
        // 第二行的 paidTotalAmount 为空时该字段自动缺席
        assertEquals(1, rows.get(1).getColumns().size());
    }

    @Test
    public void customIdFieldName() {
        // 行 id 字段名可自定义（如 uid），结果 rowId 取自该字段
        UidRow row = new UidRow();
        row.uid = 88L;
        row.amount = new BigDecimal("100");
        CurrencyColumn config = config("amount", "CNY", item("USD", "0.14"));
        List<CurrencyRow> rows = processor().process(Arrays.asList(config), "uid", Arrays.asList((Object) row));
        assertEquals(1, rows.size());
        assertEquals("88", rows.get(0).getRowId());
        // 换成 id 字段名则取不到行 id：整行跳过
        assertTrue(processor().process(Arrays.asList(config), "id", Arrays.asList((Object) row)).isEmpty());
    }

    @Test
    public void blankIdFieldNameDefaultsIdField() {
        // 行 id 字段名传空白（null / 空白串）按 "id" 处理
        BillRow row = row(1L, "100");
        CurrencyColumn config = config("billTotalAmount", "CNY", item("USD", "0.14"));
        List<CurrencyRow> rows = processor().process(Arrays.asList(config), null, Arrays.asList(row));
        assertEquals(1, rows.size());
        assertEquals("1", rows.get(0).getRowId());
        rows = processor().process(Arrays.asList(config), "  ", Arrays.asList(row));
        assertEquals(1, rows.size());
        assertEquals("1", rows.get(0).getRowId());
    }

    @Test
    public void blankBaseCurrencySkipped() {
        // 基准币种空白：该字段整体跳过并告警——不默认 CNY，将错就错不如跳过（手输汇率也不救）
        FakeRateProcessor processor = new FakeRateProcessor();
        BillRow row = row(1L, "100");
        CurrencyColumn config = config("billTotalAmount", null, item("USD", "0.14"));
        assertTrue(processor().process(Arrays.asList(config), "id", Arrays.asList(row)).isEmpty());
        assertTrue(processor.asked.isEmpty());
    }

    @Test
    public void baseCurrencyFieldResolvedPerRow() {
        // 行内币种逐行不同：baseCurrencyField 指向行内币种字段，各行按自己的币种换算
        FakeRateProcessor processor = new FakeRateProcessor();
        processor.rates.put("CNY->USD", new BigDecimal("0.14"));
        processor.rates.put("EUR->USD", new BigDecimal("0.11"));
        BillRow cnyRow = currencyRow(1L, "10000", "CNY");
        BillRow eurRow = currencyRow(2L, "200", "EUR");
        CurrencyColumn config = config("billTotalAmount", null, "currency", item("USD", null));
        List<CurrencyRow> rows = processor.process(Arrays.asList(config), "id", Arrays.asList(cnyRow, eurRow));
        assertEquals(2, rows.size());
        assertEquals(0, columnOf(rows, 1L, "billTotalAmount").getTargetAmountItems().get(0)
                .getTargetAmount().compareTo(new BigDecimal("1400.00")));
        assertEquals(0, columnOf(rows, 2L, "billTotalAmount").getTargetAmountItems().get(0)
                .getTargetAmount().compareTo(new BigDecimal("22.00")));
        assertEquals("CNY", columnOf(rows, 1L, "billTotalAmount").getBaseCurrency());
        assertEquals("EUR", columnOf(rows, 2L, "billTotalAmount").getBaseCurrency());
        assertEquals(2, processor.asked.size());
    }

    @Test
    public void directBaseCurrencyWinsOverBaseCurrencyField() {
        // 二选一：baseCurrency 直接值优先，行内币种字段即使有值也不看
        FakeRateProcessor processor = new FakeRateProcessor();
        processor.rates.put("CNY->USD", new BigDecimal("0.14"));
        BillRow row = currencyRow(1L, "100", "USD");
        CurrencyColumn config = config("billTotalAmount", "CNY", "currency", item("USD", null));
        List<CurrencyRow> rows = processor.process(Arrays.asList(config), "id", Arrays.asList(row));
        assertEquals("CNY", columnOf(rows, 1L, "billTotalAmount").getBaseCurrency());
        assertEquals(0, columnOf(rows, 1L, "billTotalAmount").getTargetAmountItems().get(0)
                .getTargetAmount().compareTo(new BigDecimal("14.00")));
    }

    @Test
    public void blankRowCurrencySkipsField() {
        // 行内币种为空白：该行该字段跳过并告警，不问汇率源
        FakeRateProcessor processor = new FakeRateProcessor();
        BillRow row = currencyRow(1L, "100", " ");
        assertTrue(processor.process(
                Arrays.asList(config("billTotalAmount", null, "currency", item("USD", "0.14"))),
                "id", Arrays.asList(row)).isEmpty());
        assertTrue(processor.asked.isEmpty());
    }

    @Test
    public void unknownOrNonStringCurrencyFieldSkipsConfig() {
        // 行内币种字段名不存在 / 非字符串类型：该配置整体跳过
        FakeRateProcessor processor = new FakeRateProcessor();
        BillRow row = currencyRow(1L, "100", "CNY");
        assertTrue(processor.process(
                Arrays.asList(config("billTotalAmount", null, "notExistField", item("USD", "0.14"))),
                "id", Arrays.asList(row)).isEmpty());
        assertTrue(processor.process(
                Arrays.asList(config("billTotalAmount", null, "paidTotalAmount", item("USD", "0.14"))),
                "id", Arrays.asList(row)).isEmpty());
        assertTrue(processor.asked.isEmpty());
    }

    @Test
    public void serverRateResolvedAndEchoed() {
        FakeRateProcessor processor = new FakeRateProcessor();
        processor.rates.put("CNY->USD", new BigDecimal("0.14"));
        BillRow row = row(1L, "10000");
        CurrencyColumn config = config("billTotalAmount", "CNY", item("USD", null));
        List<CurrencyRow> rows = processor.process(Arrays.asList(config), "id", Arrays.asList(row));
        CurrencyColumn.TargetAmountItem usd = columnOf(rows, 1L, "billTotalAmount").getTargetAmountItems().get(0);
        // 服务端取的汇率要回显
        assertEquals(0, usd.getExchangeRate().compareTo(new BigDecimal("0.14")));
        assertEquals(0, usd.getTargetAmount().compareTo(new BigDecimal("1400.00")));
    }

    @Test
    public void serverRateMissingSkipsItem() {
        FakeRateProcessor processor = new FakeRateProcessor();
        BillRow row = row(1L, "10000");
        CurrencyColumn config = config("billTotalAmount", "CNY",
                item("USD", "0.14"), item("EUR", null));
        List<CurrencyRow> rows = processor.process(Arrays.asList(config), "id", Arrays.asList(row));
        List<CurrencyColumn.TargetAmountItem> items = columnOf(rows, 1L, "billTotalAmount").getTargetAmountItems();
        assertEquals(1, items.size());
        assertEquals("USD", items.get(0).getTargetCurrency());
    }

    @Test
    public void targetEqualsBaseShowsOriginal() {
        // 目标币种与基准币种相同：按原值展示（汇率 1，不查服务端）
        FakeRateProcessor processor = new FakeRateProcessor();
        processor.boom = true;
        BillRow row = row(1L, "100.555");
        CurrencyColumn config = config("billTotalAmount", "CNY", item("CNY", null));
        List<CurrencyRow> rows = processor.process(Arrays.asList(config), "id", Arrays.asList(row));
        CurrencyColumn.TargetAmountItem cny = columnOf(rows, 1L, "billTotalAmount").getTargetAmountItems().get(0);
        assertEquals(0, cny.getExchangeRate().compareTo(BigDecimal.ONE));
        assertEquals(0, cny.getTargetAmount().compareTo(new BigDecimal("100.56")));
        assertTrue(processor.asked.isEmpty());
    }
    // endregion


    // region ======== 白名单与跳过规则 ========

    @Test
    public void unknownAndNonNumericFieldSkipped() {
        BillRow row = row(1L, "10000");
        List<CurrencyColumn> currencyColumns = Arrays.asList(
                config("notExistField", "CNY", item("USD", "0.14")),
                config("name", "CNY", item("USD", "0.14")));
        assertTrue(processor().process(currencyColumns, "id", Arrays.asList(row)).isEmpty());
    }

    @Test
    public void nullAmountAndMissingIdSkipped() {
        BillRow noId = row(null, "100");
        BillRow noAmount = row(2L, null);
        BillRow normal = row(3L, "50");
        CurrencyColumn config = config("billTotalAmount", "CNY", item("USD", "0.14"));
        List<CurrencyRow> rows = processor()
                .process(Arrays.asList(config), "id", Arrays.asList(noId, noAmount, normal));
        assertEquals(1, rows.size());
        assertEquals("3", rows.get(0).getRowId());
        assertNotNull(columnOf(rows, 3L, "billTotalAmount"));
    }

    @Test
    public void nonPositiveManualRateSkipped() {
        BillRow row = row(1L, "10000");
        // 手输汇率 0 / 负数均非法：两个币种都被跳过，整体无换算结果
        CurrencyColumn config = config("billTotalAmount", "CNY",
                item("USD", "0"), item("EUR", "-1"));
        assertTrue(processor().process(Arrays.asList(config), "id", Arrays.asList(row)).isEmpty());
    }
    // endregion


    // region ======== 归一 / 精度 / 门面 ========

    @Test
    public void currencyNormalizeAndAmountScale() {
        BillRow row = row(1L, "10000");
        CurrencyColumn.TargetAmountItem raw = item(" usd ", "0.14");
        raw.setAmountScale(3);
        CurrencyColumn config = config("billTotalAmount", "cny", raw);
        List<CurrencyRow> rows = processor().process(Arrays.asList(config), "id", Arrays.asList(row));
        CurrencyColumn column = columnOf(rows, 1L, "billTotalAmount");
        assertEquals("CNY", column.getBaseCurrency());
        CurrencyColumn.TargetAmountItem usd = column.getTargetAmountItems().get(0);
        assertEquals("USD", usd.getTargetCurrency());
        assertEquals(0, usd.getTargetAmount().compareTo(new BigDecimal("1400.000")));
        assertEquals(3, usd.getTargetAmount().scale());
        assertEquals((Integer) 3, usd.getAmountScale());
    }

    @Test
    public void facadeDispatchesToRegisteredProcessor() {
        FakeRateProcessor processor = new FakeRateProcessor();
        processor.rates.put("CNY->USD", new BigDecimal("0.14"));
        CurrencyUtil.setCurrencyColumnProcessor(processor);
        BillRow row = row(1L, "100");
        CurrencyColumn config = config("billTotalAmount", "CNY", item("USD", null));
        List<CurrencyRow> rows = CurrencyUtil
                .processCurrencyColumns(Arrays.asList(config), "id", Arrays.asList(row));
        assertEquals(1, rows.size());
        assertEquals(1, processor.asked.size());
        assertEquals("CNY->USD", processor.asked.get(0));
    }
    // endregion


    // region ======== 测试数据与工具 ========

    /** 覆写服务端汇率解析的假处理器（记录询问、可指定返回或抛错） */
    private static class FakeRateProcessor extends SimpleCurrencyColumnProcessor {
        final Map<String, BigDecimal> rates = new HashMap<>();
        final List<String> asked = new ArrayList<>();
        boolean boom = false;

        @Override
        protected BigDecimal resolveRate(Object row, String baseCurrency, String targetCurrency) {
            asked.add(baseCurrency + "->" + targetCurrency);
            if (boom) { throw new IllegalStateException("should not be called"); }
            return rates.get(baseCurrency + "->" + targetCurrency);
        }
    }

    private static CurrencyColumn columnOf(List<CurrencyRow> rows, Object rowId, String amountField) {
        for (CurrencyRow row : rows) {
            if (row.getRowId().equals(String.valueOf(rowId))) {
                for (CurrencyColumn column : row.getColumns()) {
                    if (column.getAmountField().equals(amountField)) { return column; }
                }
            }
        }
        return null;
    }

    private static SimpleCurrencyColumnProcessor processor() {

        return new SimpleCurrencyColumnProcessor();
    }

    private static BillRow row(Long id, String amount) {
        BillRow row = new BillRow();
        row.id = id;
        row.billTotalAmount = amount == null ? null : new BigDecimal(amount);
        return row;
    }

    private static BillRow currencyRow(Long id, String amount, String currency) {
        BillRow row = row(id, amount);
        row.currency = currency;
        return row;
    }

    private static CurrencyColumn config(String amountField, String baseCurrency, CurrencyColumn.TargetAmountItem... items) {
        return config(amountField, baseCurrency, null, items);
    }

    private static CurrencyColumn config(String amountField, String baseCurrency, String baseCurrencyField,
                                         CurrencyColumn.TargetAmountItem... items) {
        CurrencyColumn config = new CurrencyColumn();
        config.setAmountField(amountField);
        config.setBaseCurrency(baseCurrency);
        config.setBaseCurrencyField(baseCurrencyField);
        config.setTargetAmountItems(new ArrayList<>(Arrays.asList(items)));
        return config;
    }

    private static CurrencyColumn.TargetAmountItem item(String targetCurrency, String exchangeRate) {
        CurrencyColumn.TargetAmountItem item = new CurrencyColumn.TargetAmountItem();
        item.setTargetCurrency(targetCurrency);
        item.setExchangeRate(exchangeRate == null ? null : new BigDecimal(exchangeRate));
        return item;
    }

    /**
     * 模拟列表页的结果 DTO（金额 BigDecimal，纯内存反射读写；currency 为行内币种字段）.
     */
    private static class BillRow {
        private Long id;
        private BigDecimal billTotalAmount;
        private BigDecimal paidTotalAmount;
        private String name;
        private String currency;
    }

    /**
     * 行 id 字段名不是 id 的模拟 DTO.
     */
    private static class UidRow {
        private Long uid;
        private BigDecimal amount;
    }
    // endregion

}
