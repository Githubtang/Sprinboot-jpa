package com.example.springboottyy.security.handle;

import com.example.springboottyy.utils.ApiResponse;
import com.example.springboottyy.utils.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/9/20 23:09
 * @Version: 1.0
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public LogoutSuccessHandlerImpl(JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        /**
         * Todo 清除token,用户日志
         * @return json
         */
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        jwtUtil.invalidateToken(token);
        String jsonResponse = objectMapper.writeValueAsString(ApiResponse.success("Logout successful", null)); // 将ApiResponse对象序列化为JSON
        response.getWriter().write(jsonResponse); // 将JSON写入响应体

    }
}
