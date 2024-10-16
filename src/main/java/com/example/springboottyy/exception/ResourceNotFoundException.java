package com.example.springboottyy.exception;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/13 0:32
 * @Version: 1.0
 */

public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
