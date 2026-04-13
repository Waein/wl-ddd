package com.waein.domain.order.entity;

import com.waein.domain.common.model.Aggregate;
import com.waein.types.dp.*;
import com.waein.types.exception.ValidationException;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 订单 - Aggregate Root
 * 包含有状态的业务行为和业务校验逻辑
 * 生命周期仅在内存中，不需要可序列化
 */
@Getter
public class Order implements Aggregate<OrderId> {

    @Setter
    private OrderId id;
    private UserId userId;
    private List<LineItem> lineItems;
    private OrderStatus status;
    private Money totalAmount;
    private LocalDateTime createTime;
    @Setter
    private LocalDateTime updateTime;

    public Order(UserId userId) {
        this.userId = userId;
        this.lineItems = new ArrayList<>();
        this.status = OrderStatus.CREATED;
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
        this.totalAmount = Money.ofCny(java.math.BigDecimal.ZERO);
    }

    /**
     * 供Repository反序列化时使用的构造器
     */
    public Order(OrderId id, UserId userId, OrderStatus status,
                 Money totalAmount, LocalDateTime createTime, LocalDateTime updateTime) {
        this.id = id;
        this.userId = userId;
        this.lineItems = new ArrayList<>();
        this.status = status;
        this.totalAmount = totalAmount;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    @Override
    public OrderId getId() {
        return this.id;
    }

    public List<LineItem> getLineItems() {
        return Collections.unmodifiableList(lineItems);
    }

    /**
     * 添加子订单行项
     */
    public void addLineItem(ItemId itemId, String itemName, Quantity quantity, Money price) {
        assertModifiable();
        LineItem item = new LineItem(itemId, itemName, quantity, price);
        this.lineItems.add(item);
        recalculateTotal();
    }

    public void addLineItem(LineItem lineItem) {
        assertModifiable();
        this.lineItems.add(lineItem);
        recalculateTotal();
    }

    /**
     * 用于Repository重建时直接加入LineItem，跳过状态校验
     */
    public void restoreLineItem(LineItem lineItem) {
        this.lineItems.add(lineItem);
    }

    /**
     * 移除子订单行项
     */
    public void removeLineItem(ItemId itemId) {
        assertModifiable();
        this.lineItems.removeIf(item -> item.getItemId().equals(itemId));
        recalculateTotal();
    }

    /**
     * 支付操作 - 业务行为封装在Entity内部
     */
    public void pay() {
        if (this.status != OrderStatus.CREATED) {
            throw new ValidationException("Order", "只有待支付订单才能支付，当前状态: " + status.getDescription());
        }
        if (lineItems.isEmpty()) {
            throw new ValidationException("Order", "订单中没有商品，无法支付");
        }
        this.status = OrderStatus.PAID;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 发货操作
     */
    public void deliver() {
        if (this.status != OrderStatus.PAID) {
            throw new ValidationException("Order", "只有已支付订单才能发货，当前状态: " + status.getDescription());
        }
        this.status = OrderStatus.DELIVERED;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 完成订单
     */
    public void complete() {
        if (this.status != OrderStatus.DELIVERED) {
            throw new ValidationException("Order", "只有已发货订单才能完成，当前状态: " + status.getDescription());
        }
        this.status = OrderStatus.COMPLETED;
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 取消订单
     */
    public void cancel() {
        if (this.status == OrderStatus.COMPLETED || this.status == OrderStatus.CANCELLED) {
            throw new ValidationException("Order", "已完成或已取消的订单不能取消");
        }
        this.status = OrderStatus.CANCELLED;
        this.updateTime = LocalDateTime.now();
    }

    private void assertModifiable() {
        if (this.status != OrderStatus.CREATED) {
            throw new ValidationException("Order", "只有待支付状态的订单才能修改");
        }
    }

    private void recalculateTotal() {
        Money total = Money.ofCny(java.math.BigDecimal.ZERO);
        for (LineItem item : lineItems) {
            total = total.add(item.subtotal());
        }
        this.totalAmount = total;
    }
}
