package com.waein.infrastructure.order.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.waein.domain.order.entity.Order;
import com.waein.domain.order.repository.OrderRepository;
import com.waein.infrastructure.order.converter.OrderDataConverter;
import com.waein.infrastructure.order.dataobject.LineItemDO;
import com.waein.infrastructure.order.dataobject.OrderDO;
import com.waein.infrastructure.order.mapper.LineItemMapper;
import com.waein.infrastructure.order.mapper.OrderMapper;
import com.waein.types.dp.OrderId;
import com.waein.types.dp.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单Repository实现类 - 在Infrastructure层
 * 负责Entity和DO之间的转化以及数据库操作
 * 使用中性的 save 方法内部判断 insert/update
 */
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderMapper orderMapper;
    private final LineItemMapper lineItemMapper;
    private final OrderDataConverter converter = OrderDataConverter.INSTANCE;

    @Override
    public void attach(Order aggregate) {
        // 简化版不实现Change-Tracking，可后续扩展
    }

    @Override
    public void detach(Order aggregate) {
        // 简化版不实现Change-Tracking
    }

    @Override
    public Order find(OrderId id) {
        OrderDO orderDO = orderMapper.selectById(id.getValue());
        if (orderDO == null) {
            return null;
        }
        List<LineItemDO> lineItemDOs = lineItemMapper.selectByOrderId(orderDO.getId());
        return converter.fromData(orderDO, lineItemDOs);
    }

    @Override
    public void remove(Order aggregate) {
        if (aggregate.getId() != null) {
            lineItemMapper.deleteByOrderId(aggregate.getId().getValue());
            orderMapper.deleteById(aggregate.getId().getValue());
        }
    }

    @Override
    public void save(Order aggregate) {
        OrderDO orderDO = converter.toData(aggregate);

        if (aggregate.getId() != null && aggregate.getId().getValue() > 0) {
            // Update
            orderMapper.updateById(orderDO);
            // 先删后插子订单行项（简化实现，实际可结合Change-Tracking优化）
            lineItemMapper.deleteByOrderId(orderDO.getId());
        } else {
            // Insert
            orderMapper.insert(orderDO);
        }

        // 保存行项
        for (LineItemDO itemDO : converter.toLineItemDataList(aggregate)) {
            itemDO.setOrderId(orderDO.getId());
            lineItemMapper.insert(itemDO);
        }

        // 回写ID到Entity
        aggregate.setId(new OrderId(orderDO.getId()));
    }

    @Override
    public List<Order> findByUserId(UserId userId) {
        List<OrderDO> orderDOs = orderMapper.selectList(
                new LambdaQueryWrapper<OrderDO>()
                        .eq(OrderDO::getUserId, userId.getValue())
                        .orderByDesc(OrderDO::getCreateTime)
        );
        return orderDOs.stream().map(orderDO -> {
            List<LineItemDO> items = lineItemMapper.selectByOrderId(orderDO.getId());
            return converter.fromData(orderDO, items);
        }).collect(Collectors.toList());
    }

    @Override
    public long countByUserId(UserId userId) {
        return orderMapper.selectCount(
                new LambdaQueryWrapper<OrderDO>()
                        .eq(OrderDO::getUserId, userId.getValue())
        );
    }
}
