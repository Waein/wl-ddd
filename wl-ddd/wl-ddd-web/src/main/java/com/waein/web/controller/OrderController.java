package com.waein.web.controller;

import com.waein.application.common.ApiResponse;
import com.waein.application.order.dto.OrderCreateCommand;
import com.waein.application.order.dto.OrderDTO;
import com.waein.application.order.service.OrderApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 订单Controller
 * Controller层逻辑极简，仅做参数接收和Application Service调用
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    @PostMapping
    public ApiResponse<OrderDTO> createOrder(@RequestBody @Valid OrderCreateCommand command) {
        return ApiResponse.success(orderApplicationService.createOrder(command));
    }

    @GetMapping("/{orderId}")
    public ApiResponse<OrderDTO> getOrder(@PathVariable Long orderId) {
        return ApiResponse.success(orderApplicationService.getOrder(orderId));
    }

    @GetMapping
    public ApiResponse<List<OrderDTO>> listByUserId(@RequestParam Long userId) {
        return ApiResponse.success(orderApplicationService.listByUserId(userId));
    }

    @PostMapping("/{orderId}/pay")
    public ApiResponse<Void> payOrder(@PathVariable Long orderId) {
        orderApplicationService.payOrder(orderId);
        return ApiResponse.success(null);
    }

    @PostMapping("/{orderId}/cancel")
    public ApiResponse<Void> cancelOrder(@PathVariable Long orderId) {
        orderApplicationService.cancelOrder(orderId);
        return ApiResponse.success(null);
    }
}
