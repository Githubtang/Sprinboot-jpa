package com.example.springboottyy.controller;

import com.example.springboottyy.model.SysMenu;
import com.example.springboottyy.service.MenuService;
import com.example.springboottyy.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Insight
 * @Description: 菜单controller
 * @Date: 2024/10/14 7:55
 * @Version: 1.0
 */
@Tag(name = "菜单管理")
@RestController
@RequestMapping("/api/menu")
public class MenuController {
    private static final Logger log = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @Operation(summary = "菜单列表")
    @PreAuthorize("@ss.hasPermi('system:menu:list')")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> getAllMenu() {
        ApiResponse<?> response = menuService.findAll();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "查询菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @PostMapping("/getMenu")
    public ResponseEntity<ApiResponse<?>> getMenuById(@RequestBody Long id) {
        ApiResponse<?> response = menuService.findById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "创建菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @PostMapping("/createMenu")
    public ResponseEntity<ApiResponse<?>> createMenu(@Valid @RequestBody SysMenu menu) {
        ApiResponse<?> permission1 = menuService.createMenu(menu);
        return ResponseEntity.ok(permission1);
    }

    @Operation(summary = "修改菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @PostMapping("/updateMenu")
    public ResponseEntity<ApiResponse<?>> updateMenu(@RequestBody SysMenu menu) {
        ApiResponse<?> permission1 = menuService.updateMenu(menu);
        return ResponseEntity.ok(permission1);
    }

    @Operation(summary = "删除菜单")
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @PostMapping("/deleteMenu")
    public ResponseEntity<ApiResponse<?>> deleteMenu(@RequestBody Long id) {
        ApiResponse<?> response = menuService.softDeleteMenu(id);
        return ResponseEntity.ok(response);
    }

}
