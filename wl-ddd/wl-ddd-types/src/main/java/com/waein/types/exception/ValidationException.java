package com.waein.types.exception;

/**
 * Domain Primitive校验异常，在DP创建时如果参数不合法则抛出
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String field, String message) {
        super(String.format("[%s] %s", field, message));
    }
}
