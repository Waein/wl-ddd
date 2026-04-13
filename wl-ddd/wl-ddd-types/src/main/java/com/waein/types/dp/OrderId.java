package com.waein.types.dp;

import com.waein.types.exception.ValidationException;
import lombok.Value;

/**
 * 订单ID - Domain Primitive
 */
@Value
public class OrderId implements Identifier {

    private static final long serialVersionUID = 1L;

    Long value;

    public OrderId(Long value) {
        if (value == null || value <= 0) {
            throw new ValidationException("OrderId", "必须大于0");
        }
        this.value = value;
    }
}
