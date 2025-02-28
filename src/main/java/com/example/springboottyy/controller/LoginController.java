package com.example.springboottyy.controller;

import com.example.springboottyy.dto.request.LoginRequest;
import com.example.springboottyy.model.SysMenu;
import com.example.springboottyy.model.SysUser;
import com.example.springboottyy.model.vo.RouterVo;
import com.example.springboottyy.service.LoginService;
import com.example.springboottyy.service.MenuService;
import com.example.springboottyy.service.SysPermissionService;
import com.example.springboottyy.utils.ApiResponse;
import com.example.springboottyy.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

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
    private SysPermissionService permissionService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

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
        SysUser user = SecurityUtils.getLoginUser().getUser();
        Set<String> roles = permissionService.getRolePermission(user);
        Set<String> permissions = permissionService.getMenuPermission(user);
        ApiResponse<Object> response = ApiResponse.success("用户信息");
        response.put("user",user);
        response.put("roles",roles);
        response.put("permissions",permissions);
        return ResponseEntity.ok(response);
    }

    /**
     * 获取路由
     */
    @PostMapping("/getRouters")
    public ResponseEntity<ApiResponse<?>> getRouters() {
        Long userId = SecurityUtils.getUserId();
        ApiResponse<List<SysMenu>> menus = menuService.selectMenuTreeByUserId(userId);
        List<RouterVo> voList = menuService.buildMenus(menus.getData());
        return ResponseEntity.ok(ApiResponse.success("查询成功",voList));
    }
}
