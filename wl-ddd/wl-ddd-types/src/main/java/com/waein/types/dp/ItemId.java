package com.waein.types.dp;

import com.waein.types.exception.ValidationException;
import lombok.Value;

/**
 * 商品ID - Domain Primitive
 */
@Value
public class ItemId implements Identifier {

    private static final long serialVersionUID = 1L;

    Long value;

    public ItemId(Long value) {
        if (value == null || value <= 0) {
            throw new ValidationException("ItemId", "必须大于0");
        }
        this.value = value;
    }
}
