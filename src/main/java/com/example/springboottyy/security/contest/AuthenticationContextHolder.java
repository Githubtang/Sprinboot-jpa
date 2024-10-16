package com.example.springboottyy.security.contest;

import org.springframework.security.core.Authentication;

/**
 * @Author: Insight
 * @Description: TODO 身份信息
 * @Date: 2024/9/25 20:24
 * @Version: 1.0
 */
public class AuthenticationContextHolder {
    private static final ThreadLocal<Authentication> contextHolder = new ThreadLocal<>();

    public static Authentication getContext() {
        return contextHolder.get();
    }

    public static void setContext(Authentication context) {
        contextHolder.set(context);
    }

    public static void clearContext() {
        contextHolder.remove();
    }
}
