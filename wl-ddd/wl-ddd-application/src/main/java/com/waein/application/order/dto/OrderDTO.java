package com.waein.application.order.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单DTO - 出参，由DTO Assembler从多个Entity组装
 */
@Data
public class OrderDTO {

    private Long orderId;
    private Long userId;
    private String status;
    private String statusDesc;
    private BigDecimal totalAmount;
    private String currency;
    private List<LineItemDTO> items;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;

    @Data
    public static class LineItemDTO {
        private Long itemId;
        private String itemName;
        private int quantity;
        private BigDecimal price;
        private BigDecimal subtotal;
    }
}
