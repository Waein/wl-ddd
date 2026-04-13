package com.waein.application.order.assembler;

import com.waein.application.order.dto.OrderDTO;
import com.waein.domain.order.entity.LineItem;
import com.waein.domain.order.entity.Order;

import java.util.stream.Collectors;

/**
 * DTO Assembler - 将Entity转化为DTO
 * 按照 P of EAA 的标准命名
 * 核心作用：将1个或多个关联的Entity转化为1个或多个DTO
 */
public class OrderDtoAssembler {

    public static OrderDTO toDTO(Order order) {
        if (order == null) {
            return null;
        }
        OrderDTO dto = new OrderDTO();
        dto.setOrderId(order.getId() != null ? order.getId().getValue() : null);
        dto.setUserId(order.getUserId().getValue());
        dto.setStatus(order.getStatus().name());
        dto.setStatusDesc(order.getStatus().getDescription());
        dto.setTotalAmount(order.getTotalAmount().getAmount());
        dto.setCurrency(order.getTotalAmount().getCurrency().getCurrencyCode());
        dto.setCreateTime(order.getCreateTime());
        dto.setUpdateTime(order.getUpdateTime());
        dto.setItems(order.getLineItems().stream()
                .map(OrderDtoAssembler::toLineItemDTO)
                .collect(Collectors.toList()));
        return dto;
    }

    private static OrderDTO.LineItemDTO toLineItemDTO(LineItem item) {
        OrderDTO.LineItemDTO dto = new OrderDTO.LineItemDTO();
        dto.setItemId(item.getItemId().getValue());
        dto.setItemName(item.getItemName());
        dto.setQuantity(item.getQuantity().getValue());
        dto.setPrice(item.getPrice().getAmount());
        dto.setSubtotal(item.subtotal().getAmount());
        return dto;
    }
}
