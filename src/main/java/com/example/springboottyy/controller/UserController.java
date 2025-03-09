package com.example.springboottyy.controller;

import com.example.springboottyy.dto.request.AuthDeptRequest;
import com.example.springboottyy.dto.request.AuthPostRequest;
import com.example.springboottyy.dto.request.AuthRoleRequest;
import com.example.springboottyy.dto.request.LoginRequest;
import com.example.springboottyy.model.SysUser;
import com.example.springboottyy.service.UserService;
import com.example.springboottyy.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Insight
 * @Description: 用户controller
 * @Date: 2024/10/13 1:32
 * @Version: 1.0
 */
@Tag(name = "用户管理")
@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Operation(summary = "获取用户列表")
    @PreAuthorize("@ss.hasPermi('system:user:list')")
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllUsers() {
        ApiResponse<?> response = userService.findAll();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "获取用户")
    @PreAuthorize("@ss.hasAnyPermi('system:user:query')")
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getUserById(@PathVariable Long id) {
        ApiResponse<?> response = userService.getUserById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "创建用户")
    @PreAuthorize("@ss.hasPermi('system:user:add')")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createUser(@Valid @RequestBody SysUser user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @Operation(summary = "删除用户")
    @PreAuthorize("@ss.hasPermi('system:user:remove')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Boolean>> softDeleteUser(@PathVariable Long id) {
        ApiResponse<Boolean> response = userService.softDeleteUser(id);
        return ResponseEntity.ok(response);
    }

    /* 开启全部用户 */
    @Operation(summary = "开启全部用户")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PostMapping("/upUser")
    public ResponseEntity<ApiResponse<ArrayList<SysUser>>> upUser() {
        ApiResponse<ArrayList<SysUser>> response = userService.upUser();
        return ResponseEntity.ok(response);
    }

    /* 关闭用户 */
    @Operation(summary = "关闭用户")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PostMapping("/downUsers")
    public ResponseEntity<ApiResponse<ArrayList<SysUser>>> downUsers(@RequestBody List<Long> ids) {
        ApiResponse<ArrayList<SysUser>> response = userService.downUsers(ids);
        return ResponseEntity.ok(response);
    }

    /* 添加角色 */
    @Operation(summary = "添加角色")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PostMapping("/addAuthRole")
    public ResponseEntity<ApiResponse<?>> addRole(@Valid @RequestBody AuthRoleRequest authRoleRequest) {
        ApiResponse<?> response = userService.addUserRole(authRoleRequest.getUserId(), authRoleRequest.getRoleId());
        return ResponseEntity.ok(response);
    }

    /* 添加部门 */
    @Operation(summary = "添加部门")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PostMapping("/addDept")
    public ResponseEntity<ApiResponse<?>> addPermission(@Valid @RequestBody AuthDeptRequest deptRequest) {
        ApiResponse<SysUser> response = userService.addUserDept(deptRequest.getUserId(), deptRequest.getDeptId());
        return ResponseEntity.ok(response);
    }

    /* 添加岗位 */
    @Operation(summary = "添加岗位")
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @PostMapping("/addPost")
    public ResponseEntity<ApiResponse<?>> addDept(@Valid @RequestBody AuthPostRequest postRequest) {
        ApiResponse<?> response = userService.addUserPost(postRequest.getUserId(), postRequest.getPostIds());
        return ResponseEntity.ok(response);
    }

}
