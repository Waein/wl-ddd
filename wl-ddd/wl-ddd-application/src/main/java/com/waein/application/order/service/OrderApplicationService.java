package com.waein.application.order.service;

import com.waein.application.order.dto.OrderCreateCommand;
import com.waein.application.order.dto.OrderDTO;

import java.util.List;

/**
 * 订单应用服务接口
 * Application Service 仅包含 Orchestration（编排），所有逻辑均delegate到Domain层
 * Application Service 的入参出参是DTO，不暴露Entity
 */
public interface OrderApplicationService {

    /**
     * 创建订单
     */
    OrderDTO createOrder(OrderCreateCommand command);

    /**
     * 根据订单ID查询
     */
    OrderDTO getOrder(Long orderId);

    /**
     * 根据用户ID查询订单列表
     */
    List<OrderDTO> listByUserId(Long userId);

    /**
     * 支付订单
     */
    void payOrder(Long orderId);

    /**
     * 取消订单
     */
    void cancelOrder(Long orderId);
}
