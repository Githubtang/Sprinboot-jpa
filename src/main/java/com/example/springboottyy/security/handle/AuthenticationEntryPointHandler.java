package com.example.springboottyy.security.handle;

import com.alibaba.fastjson2.JSON;
import com.example.springboottyy.common.constant.HttpStatus;
import com.example.springboottyy.utils.ApiResponse;
import com.example.springboottyy.utils.ServletUtils;
import com.example.springboottyy.utils.StringUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        int code = HttpStatus.UNAUTHORIZED;
        String message = StringUtils.format("请求访问 : {},认证失败,无法访问系统资源", request.getRequestURI());
        ServletUtils.renderString(response, JSON.toJSONString(ApiResponse.error(code,message)));
    }

    /*  version : 1.0
    *//*用户没有登录*//*
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 设置HTTP状态码401
        response.setContentType("application/json"); // 设置内容类型为JSON

        ApiResponse<Object> msg = new ApiResponse<>("401", "Unauthorized: Authentication token is missing or invalid", null);
        String jsonResponse = objectMapper.writeValueAsString(msg); // 将ApiResponse对象序列化为JSON

        response.getWriter().write(jsonResponse); // 将JSON写入响应体
    }

    *//*用户没有权限*//*
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ApiResponse<Object> msg = new ApiResponse<>("403", "Access Denied", null);
        String json = new ObjectMapper().writeValueAsString(msg);
        response.getWriter().write(json);
    }*/
}
