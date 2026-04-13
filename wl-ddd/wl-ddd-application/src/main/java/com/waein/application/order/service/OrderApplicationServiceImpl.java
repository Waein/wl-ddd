package com.waein.application.order.service;

import com.waein.application.order.assembler.OrderDtoAssembler;
import com.waein.application.order.dto.OrderCreateCommand;
import com.waein.application.order.dto.OrderDTO;
import com.waein.domain.order.entity.Order;
import com.waein.domain.order.repository.OrderRepository;
import com.waein.types.dp.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 订单应用服务实现
 * 仅做编排(Orchestration)，不包含业务计算逻辑:
 * 1. 参数校验（通过DP构造函数自动完成）
 * 2. 读数据
 * 3. 调用Domain层的业务逻辑
 * 4. 保存数据
 * 5. 发送消息/事件（如有需要）
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderRepository orderRepository;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public OrderDTO createOrder(OrderCreateCommand command) {
        // 1. DP自动校验入参
        UserId userId = new UserId(command.getUserId());

        // 2. 构建Aggregate Root
        Order order = new Order(userId);
        for (OrderCreateCommand.LineItemDTO item : command.getItems()) {
            order.addLineItem(
                    new ItemId(item.getItemId()),
                    item.getItemName(),
                    new Quantity(item.getQuantity()),
                    new Money(item.getPrice(), Currency.getInstance(
                            item.getCurrency() != null ? item.getCurrency() : "CNY"))
            );
        }

        // 3. 保存
        orderRepository.save(order);

        log.info("订单创建成功, orderId={}, userId={}", order.getId(), userId);

        // 4. 转化为DTO返回
        return OrderDtoAssembler.toDTO(order);
    }

    @Override
    public OrderDTO getOrder(Long orderId) {
        OrderId id = new OrderId(orderId);
        Order order = orderRepository.find(id);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在: " + orderId);
        }
        return OrderDtoAssembler.toDTO(order);
    }

    @Override
    public List<OrderDTO> listByUserId(Long userId) {
        UserId uid = new UserId(userId);
        return orderRepository.findByUserId(uid).stream()
                .map(OrderDtoAssembler::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payOrder(Long orderId) {
        OrderId id = new OrderId(orderId);
        Order order = orderRepository.find(id);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在: " + orderId);
        }

        // 业务逻辑封装在Entity内部
        order.pay();

        orderRepository.save(order);
        log.info("订单支付成功, orderId={}", orderId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId) {
        OrderId id = new OrderId(orderId);
        Order order = orderRepository.find(id);
        if (order == null) {
            throw new IllegalArgumentException("订单不存在: " + orderId);
        }

        order.cancel();

        orderRepository.save(order);
        log.info("订单取消成功, orderId={}", orderId);
    }
}
