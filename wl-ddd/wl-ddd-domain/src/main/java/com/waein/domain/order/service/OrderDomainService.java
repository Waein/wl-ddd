package com.waein.domain.order.service;

import com.waein.domain.order.entity.Order;
import com.waein.types.dp.Money;

/**
 * 订单领域服务 - 封装跨Entity/多对象的业务逻辑
 * Domain Service不应包含数据存储逻辑，只包含纯业务计算
 */
public class OrderDomainService {

    /**
     * 校验订单金额是否在允许范围内
     */
    public void validateOrderAmount(Order order, Money maxOrderLimit) {
        if (order.getTotalAmount().isGreaterThan(maxOrderLimit)) {
            throw new IllegalArgumentException("订单金额超出限制: " + maxOrderLimit.getAmount());
        }
    }
}
