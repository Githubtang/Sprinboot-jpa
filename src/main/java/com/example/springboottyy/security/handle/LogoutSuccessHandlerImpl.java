package com.example.springboottyy.security.handle;

import com.alibaba.fastjson2.JSON;
import com.example.springboottyy.common.constant.Constants;
import com.example.springboottyy.manager.AsyncManager;
import com.example.springboottyy.manager.factory.AsyncFactory;
import com.example.springboottyy.model.LoginUser;
import com.example.springboottyy.utils.*;
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
        LoginUser loginUser = jwtUtil.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            jwtUtil.deleteLoginUser(loginUser.getToken());
            // 记录用户退出日志
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(userName, Constants.LOGOUT,
                    MessageUtils.message("user.logout.success")));
        }
        ServletUtils.renderString(response,
                JSON.toJSONString(ApiResponse.success(MessageUtils.message("user.logout.success"))));
    }


/*  version: 1.0
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=utf-8");
        *//**
         * Todo 清除token,用户日志
         * @return json
         *//*
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        jwtUtil.invalidateToken(token);
        String jsonResponse = objectMapper.writeValueAsString(ApiResponse.success("Logout successful", null)); // 将ApiResponse对象序列化为JSON
        response.getWriter().write(jsonResponse); // 将JSON写入响应体

    }*/
}
