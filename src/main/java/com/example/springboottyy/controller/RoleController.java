package com.example.springboottyy.controller;

import com.example.springboottyy.dto.request.AuthMenuRequest;
import com.example.springboottyy.model.SysRole;
import com.example.springboottyy.service.RoleService;
import com.example.springboottyy.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Insight
 * @Description: 角色controller
 * @Date: 2024/10/14 7:50
 * @Version: 1.0
 */
@Tag(name = "角色管理")
@RestController
@RequestMapping("/api/role")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @Operation(summary = "获取角色列表")
    @PreAuthorize("@ss.hasAnyPermi('system:role:list')")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> getAllRoles() {
        List<SysRole> all = roleService.findAll();
        ApiResponse<?> response = new ApiResponse<>(200, "success", all);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "获取角色")
    @PreAuthorize("@ss.hasAnyPermi('system:role:query')")
    @PostMapping("/getRole")
    public ResponseEntity<ApiResponse<?>> getRole(@RequestBody Long roleId) {
        SysRole byId = roleService.findById(roleId);
        ApiResponse<?> response = new ApiResponse<>(200, "success", byId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "创建角色")
    @PreAuthorize("@ss.hasAnyPermi('system:role:add')")
    @PostMapping("/createRole")
    public ResponseEntity<ApiResponse<?>> addRole(@RequestBody SysRole role) {
        SysRole role1 = roleService.createRole(role);
        ApiResponse<?> response = new ApiResponse<>(200, "success", role1);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "修改角色")
    @PreAuthorize("@ss.hasAnyPermi('system:role:edit')")
    @PostMapping("/updateRole")
    public ResponseEntity<ApiResponse<?>> updateRole(@RequestBody SysRole role) {
        ApiResponse<SysRole> response = roleService.updateRole(role);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "删除角色")
    @PreAuthorize("@ss.hasAnyPermi('system:role:remove')")
    @PostMapping("/deleteRole")
    public ResponseEntity<ApiResponse<?>> deleteRole(@RequestBody Long roleId) {
        ApiResponse<?> response = roleService.softDeleteRole(roleId);
        return ResponseEntity.ok(response);
    }

    /**
     * 角色添加权限
     * @param menuRequest
     * @return menu
     */
    @Operation(summary = "添加菜单 -> 角色")
    @PreAuthorize("@ss.hasAnyPermi('system:role:edit')")
    @PostMapping("/addMenuToRole")
    public ResponseEntity<ApiResponse<?>> addPermissionToRole(@RequestBody AuthMenuRequest menuRequest)  {
        ApiResponse<?> response = roleService.addMenuToRole(menuRequest.getRoleId(), menuRequest.getMenuIds());
        return ResponseEntity.ok(response);
    }

}
