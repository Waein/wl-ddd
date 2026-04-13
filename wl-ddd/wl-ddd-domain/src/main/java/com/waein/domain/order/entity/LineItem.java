package com.waein.domain.order.entity;

import com.waein.domain.common.model.Entity;
import com.waein.types.dp.ItemId;
import com.waein.types.dp.Money;
import com.waein.types.dp.Quantity;
import lombok.Getter;
import lombok.Setter;

/**
 * 子订单行项 - Entity（非聚合根）
 * 通过Order聚合根来管理，不能独立于Order操作
 */
@Getter
@Setter
public class LineItem implements Entity<ItemId> {

    private Long id;
    private ItemId itemId;
    private String itemName;
    private Quantity quantity;
    private Money price;

    public LineItem(ItemId itemId, String itemName, Quantity quantity, Money price) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public ItemId getId() {
        return this.itemId;
    }

    /**
     * 计算小计金额
     */
    public Money subtotal() {
        return price.multiply(java.math.BigDecimal.valueOf(quantity.getValue()));
    }

    /**
     * 修改数量
     */
    public void changeQuantity(Quantity newQuantity) {
        this.quantity = newQuantity;
    }
}
