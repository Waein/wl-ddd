package com.waein.types.dp;

import com.waein.types.exception.ValidationException;

/**
 * 订单状态 - Domain Primitive
 */
public enum OrderStatus {

    CREATED(0, "待支付"),
    PAID(1, "已支付"),
    DELIVERED(2, "已发货"),
    COMPLETED(3, "已完成"),
    CANCELLED(4, "已取消");

    private final int code;
    private final String description;

    OrderStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static OrderStatus fromCode(int code) {
        for (OrderStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new ValidationException("OrderStatus", "无效的订单状态码: " + code);
    }
}
