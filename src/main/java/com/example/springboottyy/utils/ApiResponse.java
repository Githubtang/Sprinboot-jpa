package com.example.springboottyy.utils;

import com.example.springboottyy.common.constant.HttpStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * @Author: Insight
 * @Description: 响应工具类
 * @Date: 2024/10/12 23:58
 * @Version: 1.0
 */
@Setter
@Getter
public class ApiResponse<T> extends HashMap<String, Object> {
    private int status;
    private String message;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(int status, String message, T data) {
        super.put("status", status);
        super.put("message", message);
        super.put("data", data);
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
     *
     * @return 成功消息
     */
    public static <T> ApiResponse<T> success() {
        return ApiResponse.success("操作成功");
    }

    public static <T> ApiResponse<T> success(String message) {
        return ApiResponse.success("操作成功", null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.success("操作成功", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(HttpStatus.SUCCESS, message, data);
    }

    /**
     * 返回警告消息
     *
     * @param message 返回内容
     * @return 警告消息
     */
    public static <T> ApiResponse<T> warning(String message) {
        return ApiResponse.warning(message, null);
    }

    public static <T> ApiResponse<T> warning(String message, T data) {
        return new ApiResponse<>(HttpStatus.WARN, message, data);
    }

    public static <T> ApiResponse<T> error() {
        return ApiResponse.error("操作失败");
    }

    public static <T> ApiResponse<T> error(String message) {
        return ApiResponse.error(message, null);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(HttpStatus.ERROR, message, data);
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(status, message, null);
    }

    public ApiResponse<T> put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}

/// **
// * 返回成功消息
// */
// public static <T> ApiResponse<T> success() {
// return ApiResponse.success("操作成功", null);
// }
//
// public static <T> ApiResponse<T> success(String message) {
// return ApiResponse.success(message, null);
// }
//
// public static <T> ApiResponse<T> success(String message, T data) {
// return new ApiResponse<>("success", message, data);
// }
//
/// **
// * 返回警告消息
// */
// public static <T> ApiResponse<T> warning(String message, T data) {
// return new ApiResponse<>("warn", message, data);
// }
//
/// **
// * 返回错误消息
// */
// public static <T> ApiResponse<T> error() {
// return ApiResponse.error("filed");
// }
//
// public static <T> ApiResponse<T> error(String message) {
// return ApiResponse.error(message, null);
// }
//
// public static <T> ApiResponse<T> error(String message, T data) {
// return new ApiResponse<>("filed", message, data);
// }
