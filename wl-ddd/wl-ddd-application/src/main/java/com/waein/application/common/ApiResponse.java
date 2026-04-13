package com.waein.application.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应格式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {

    private int code;
    private T data;
    private String details;
    private String msg;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(200, data, null, "success");
    }

    public static <T> ApiResponse<T> success(T data, String msg) {
        return new ApiResponse<>(200, data, null, msg);
    }

    public static <T> ApiResponse<T> error(int code, String msg) {
        return new ApiResponse<>(code, null, null, msg);
    }

    public static <T> ApiResponse<T> error(int code, String msg, String details) {
        return new ApiResponse<>(code, null, details, msg);
    }
}
