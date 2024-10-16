package com.example.springboottyy.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Insight
 * @Description: 响应工具类
 * @Date: 2024/10/12 23:58
 * @Version: 1.0
 */
@Setter
@Getter
public class ApiResponse<T> extends HashMap<String, Object> {
    private String status;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
        put("status", status);
        put("message", message);
        put("data", data);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * 返回成功消息
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.success("操作成功", null);
    }

    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.success(message, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>("success", message, data);
    }

    /**
     * 返回警告消息
     */
    public static <T> ApiResponse<T> warning(String message, T data) {
        return new ApiResponse<>("warn", message, data);
    }

    /**
     * 返回错误消息
     */
    public static <T> ApiResponse<T> error() {
        return ApiResponse.error("filed");
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.error(message, null);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>("filed", message, data);
    }

    public ApiResponse<T> put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
