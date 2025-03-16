package com.example.springboottyy.enums;

import org.springframework.lang.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求方法
 */
public enum HttpMethod {
    /** GET 请求 */
    GET,
    /** POST 请求 */
    HEAD,
    /** HEAD 请求 */
    POST,
    /** PUT 请求 */
    PUT,
    /** PATCH 请求 */
    PATCH,
    /** DELETE 请求 */
    DELETE,
    /** OPTIONS 请求 */
    OPTIONS,
    /** TRACE 请求 */
    TRACE;

    private static final Map<String, HttpMethod> mappings = new HashMap<>(16);

    static
    {
        for (HttpMethod httpMethod : values())
        {
            mappings.put(httpMethod.name(), httpMethod);
        }
    }

    @Nullable
    public static HttpMethod resolve(@Nullable String method)
    {
        return (method != null ? mappings.get(method) : null);
    }

    public boolean matches(String method)
    {
        return (this == resolve(method));
    }
}
