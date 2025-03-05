package com.example.springboottyy.exception;

/**
 * @Author: Insight
 * @Description: token异常
 * @Date: 2024/10/12 23:57
 * @Version: 1.0
 */
public class TokenExpiredException extends Throwable {
    public TokenExpiredException(String tokenIsExpired) {
    }
}
