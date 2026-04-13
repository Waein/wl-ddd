package com.waein.infrastructure.order.converter;

import com.waein.domain.order.entity.LineItem;
import com.waein.domain.order.entity.Order;
import com.waein.infrastructure.order.dataobject.LineItemDO;
import com.waein.infrastructure.order.dataobject.OrderDO;
import com.waein.types.dp.*;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Data Converter - Entity 和 DO 之间的转化器
 * 在Infrastructure层完成Entity到DO的映射，让Domain层和数据库完全解耦
 */
public class OrderDataConverter {

    public static final OrderDataConverter INSTANCE = new OrderDataConverter();

    public OrderDO toData(Order order) {
        OrderDO orderDO = new OrderDO();
        if (order.getId() != null) {
            orderDO.setId(order.getId().getValue());
        }
        orderDO.setUserId(order.getUserId().getValue());
        orderDO.setStatus(order.getStatus().getCode());
        orderDO.setTotalAmount(order.getTotalAmount().getAmount());
        orderDO.setCurrency(order.getTotalAmount().getCurrency().getCurrencyCode());
        orderDO.setCreateTime(order.getCreateTime());
        orderDO.setUpdateTime(order.getUpdateTime());
        return orderDO;
    }

    public Order fromData(OrderDO orderDO, List<LineItemDO> lineItemDOs) {
        Order order = new Order(
                new OrderId(orderDO.getId()),
                new UserId(orderDO.getUserId()),
                OrderStatus.fromCode(orderDO.getStatus()),
                new Money(orderDO.getTotalAmount(),
                        Currency.getInstance(orderDO.getCurrency() != null ? orderDO.getCurrency() : "CNY")),
                orderDO.getCreateTime(),
                orderDO.getUpdateTime()
        );

        if (lineItemDOs != null) {
            for (LineItemDO itemDO : lineItemDOs) {
                LineItem lineItem = fromData(itemDO);
                order.restoreLineItem(lineItem);
            }
        }

        return order;
    }

    public LineItemDO toData(LineItem item, Long orderId) {
        LineItemDO itemDO = new LineItemDO();
        if (item.getId() != null) {
            itemDO.setId(item.getId().getValue());
        }
        itemDO.setOrderId(orderId);
        itemDO.setItemId(item.getItemId().getValue());
        itemDO.setItemName(item.getItemName());
        itemDO.setQuantity(item.getQuantity().getValue());
        itemDO.setPrice(item.getPrice().getAmount());
        itemDO.setCurrency(item.getPrice().getCurrency().getCurrencyCode());
        return itemDO;
    }

    public LineItem fromData(LineItemDO itemDO) {
        LineItem item = new LineItem(
                new ItemId(itemDO.getItemId()),
                itemDO.getItemName(),
                new Quantity(itemDO.getQuantity()),
                new Money(itemDO.getPrice(),
                        Currency.getInstance(itemDO.getCurrency() != null ? itemDO.getCurrency() : "CNY"))
        );
        item.setId(itemDO.getId());
        return item;
    }

    public List<LineItemDO> toLineItemDataList(Order order) {
        Long orderId = order.getId() != null ? order.getId().getValue() : null;
        return order.getLineItems().stream()
                .map(item -> toData(item, orderId))
                .collect(Collectors.toList());
    }
}
