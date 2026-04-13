package com.waein.domain.order.repository;

import com.waein.domain.common.repository.Repository;
import com.waein.domain.order.entity.Order;
import com.waein.types.dp.OrderId;
import com.waein.types.dp.UserId;

import java.util.List;

/**
 * 订单Repository接口 - 在Domain层定义，Infrastructure层实现
 * 使用中性的 find/save/remove 语义
 */
public interface OrderRepository extends Repository<Order, OrderId> {

    /**
     * 根据用户ID查询订单列表
     */
    List<Order> findByUserId(UserId userId);

    /**
     * 根据用户ID统计订单数量
     */
    long countByUserId(UserId userId);
}
