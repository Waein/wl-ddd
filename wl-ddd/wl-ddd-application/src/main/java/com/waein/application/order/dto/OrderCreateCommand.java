package com.waein.application.order.dto;

import lombok.Data;

import java.util.List;

/**
 * 创建订单命令 - DTO (CQRS中的Command)
 * DTO适配不同的业务场景入参，避免让业务对象变成万能大对象
 */
@Data
public class OrderCreateCommand {

    private Long userId;
    private List<LineItemDTO> items;

    @Data
    public static class LineItemDTO {
        private Long itemId;
        private String itemName;
        private int quantity;
        private java.math.BigDecimal price;
        private String currency;
    }
}
