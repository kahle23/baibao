/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.data.currency.support;

import baibao.extension.tool.exchangerate.ExRateUtil;
import cn.hutool.core.util.ReflectUtil;
import kunlun.data.currency.support.DefaultCurrencyColumnProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 默认处理器的汇率源接入：继承 {@link DefaultCurrencyColumnProcessor} 的换算管线
 * （字段白名单、行 id 解析、克隆隔离、精度与归一等通用逻辑全部复用），
 * 仅覆写 {@link #resolveRate} 接入 baibao 的汇率源（{@code ExRateUtil.getRate2 → getRate1}）.
 *
 * @author Kahle
 */
public class SimpleCurrencyColumnProcessor extends DefaultCurrencyColumnProcessor {
    private static final Logger log = LoggerFactory.getLogger(SimpleCurrencyColumnProcessor.class);

    /**
     * 服务端汇率解析：getRate2 → getRate1，整体防御，
     * 无数据源 / 取数失败 / 汇率非正返回 null（调用方跳过该币种）.
     *
     * @param row 当前正在换算的行数据（提取 createTime 作为取历史汇率的时间依据）
     * @param baseCurrency 基准币种（统一大写）
     * @param targetCurrency 目标币种（统一大写）
     * @return 可用汇率，取不到返回 null
     */
    @Override
    protected BigDecimal resolveRate(Object row, String baseCurrency, String targetCurrency) {
        Date createTime = extractCreateTime(row);
        try {
            BigDecimal rate = ExRateUtil.getRate2(baseCurrency, targetCurrency, createTime);
            if (rate != null && rate.compareTo(BigDecimal.ZERO) > 0) { return rate; }
        } catch (Throwable e) {
            log.debug("CurrencyColumn: getRate2 [{} -> {}] failed", baseCurrency, targetCurrency, e);
        }
        try {
            BigDecimal rate = ExRateUtil.getRate1(baseCurrency, targetCurrency, createTime);
            if (rate != null && rate.compareTo(BigDecimal.ZERO) > 0) { return rate; }
        } catch (Throwable e) {
            log.debug("CurrencyColumn: getRate1 [{} -> {}] failed", baseCurrency, targetCurrency, e);
        }
        return null;
    }

    /**
     * 提取行数据的创建时间（取历史汇率的依据）：字段缺失、为空或非 Date 型（如 LocalDateTime）返回 null，按最新汇率取.
     */
    private Date extractCreateTime(Object row) {
        if (row == null || !ReflectUtil.hasField(row.getClass(), "createTime")) { return null; }
        Object value = ReflectUtil.getFieldValue(row, "createTime");
        return value instanceof Date ? (Date) value : null;
    }

}
