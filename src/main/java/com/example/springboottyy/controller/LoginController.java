package com.example.springboottyy.controller;

import com.example.springboottyy.dto.UserDto;
import com.example.springboottyy.dto.request.LoginRequest;
import com.example.springboottyy.model.LoginUser;
import com.example.springboottyy.model.SysRole;
import com.example.springboottyy.service.LoginService;
import com.example.springboottyy.service.MenuService;
import com.example.springboottyy.service.RoleService;
import com.example.springboottyy.service.UserService;
import com.example.springboottyy.utils.ApiResponse;
import com.example.springboottyy.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Insight
 * @Description: TODO
 * @Date: 2024/10/15 8:17
 * @Version: 1.0
 */
@Tag(name = "登录模块")
@RestController
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Operation(summary = "登录")
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest loginRequest) {
        ApiResponse<?> response = loginService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return ResponseEntity.ok(response);
    }

    /**
     * 获取用户信息
     */
    @Operation(summary = "获取用户信息")
    @PostMapping("/getInfo")
    public ResponseEntity<ApiResponse<?>> getInfo() {
        LoginUser user = SecurityUtils.getUser();
        ApiResponse<Object> response = ApiResponse.success("用户信息", user);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取路由
     */
//    @PostMapping("/getRouters")
//    public ResponseEntity<ApiResponse<?>> getRouters() {
//        Long userId = SecurityUtils.getUserId();
//        ApiResponse<?> userById = userService.getUserById(userId);
//        return ResponseEntity.ok(userById);
//
//    }
}
