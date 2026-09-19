/*
 * Copyright (c) 2019. the original author or authors.
 * BaiBao is licensed under the "LICENSE" file in the project's root directory.
 */

package baibao.common.dto.base;

import baibao.common.enums.QueryMode;
import cn.hutool.core.collection.CollUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import kunlun.common.Page;
import kunlun.data.currency.model.CurrencyColumn;
import kunlun.data.sort.SortField;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 基础的查询对象.
 * @author Kahle
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseQuery extends Page.Query implements Serializable {

    /**
     * 查询模式
     * @see QueryMode
     */
    @JsonIgnore
    private QueryMode queryMode = QueryMode.FULL;
    /**
     * 金额多币种换算配置列表（列表页按金额列配置，为空时无操作）.
     * <p>示例：[{"amountField":"billTotalAmount","baseCurrency":"CNY",
     * "targetAmountItems":[{"targetCurrency":"USD","exchangeRate":0.14},{"targetCurrency":"EUR"}]}]；
     * 基准币种与 {@code baseCurrencyField}（行内币种字段名，各行币种不同的列表页用）二选一，
     * 优先取 {@code baseCurrency} 直接值；金额字段名为结果 DTO 的属性名（驼峰，须为数值字段），经
     * {@code CurrencyUtil.processCurrencyColumns} 纯计算换算后，由
     * {@code BaseServiceImpl#applyCurrencyConversion} 把结果挂载到
     * {@code Page.others}（key：{@code currencyRows}）。
     * @see CurrencyColumn
     */
    private List<CurrencyColumn> currencyColumns;
    /**
     * 自定义排序字段列表（存储无关，顺序即排序优先级；为空时使用业务的默认排序）.
     * <p>示例：[{"field":"createTime","order":"desc"},{"field":"id","order":"asc"}]；
     * 字段名默认为实体属性名（驼峰），由排序器（{@code kunlun.data.sort.Sorter}）解析。
     */
    private List<SortField> sortFields;


    /**
     * 是否传入了金额多币种换算配置.
     *
     * @return 传入了返回 true
     */
    public boolean hasCurrencyColumns() {

        return CollUtil.isNotEmpty(currencyColumns);
    }

    /**
     * 是否传入了自定义排序.
     *
     * @return 传入了返回 true
     */
    public boolean hasCustomSort() {

        return CollUtil.isNotEmpty(sortFields);
    }

    @JsonIgnore
    @Override
    public boolean isPaged() {

        return super.isPaged();
    }

    @JsonIgnore
    @Override
    public void setPaged(boolean paged) {

        super.setPaged(paged);
    }

}
