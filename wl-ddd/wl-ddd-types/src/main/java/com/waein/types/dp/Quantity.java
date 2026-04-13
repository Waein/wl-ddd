package com.waein.types.dp;

import com.waein.types.exception.ValidationException;
import lombok.Value;

/**
 * 数量 - Domain Primitive
 */
@Value
public class Quantity {

    int value;

    public Quantity(int value) {
        if (value < 0) {
            throw new ValidationException("Quantity", "数量不能为负数");
        }
        this.value = value;
    }

    public Quantity add(Quantity other) {
        return new Quantity(this.value + other.value);
    }

    public Quantity subtract(Quantity other) {
        return new Quantity(this.value - other.value);
    }
}
