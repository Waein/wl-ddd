package com.waein.types.dp;

import com.waein.types.exception.ValidationException;
import lombok.Value;

/**
 * 用户ID - Domain Primitive
 */
@Value
public class UserId implements Identifier {

    private static final long serialVersionUID = 1L;

    Long value;

    public UserId(Long value) {
        if (value == null || value <= 0) {
            throw new ValidationException("UserId", "必须大于0");
        }
        this.value = value;
    }
}
