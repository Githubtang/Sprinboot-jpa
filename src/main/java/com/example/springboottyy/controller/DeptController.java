package com.example.springboottyy.controller;

import com.example.springboottyy.dto.DeptDto;
import com.example.springboottyy.model.SysDept;
import com.example.springboottyy.service.DeptService;
import com.example.springboottyy.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: Insight
 * @Description: 部门controller
 * @Date: 2024/10/14 8:01
 * @Version: 1.0
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("/api/dept")
public class DeptController {
    
    @Autowired
    private DeptService deptService;

    @Operation(summary = "部门列表")
    @PreAuthorize("@ss.hasPermi('system:dept:list')")
    @PostMapping
    public ResponseEntity<ApiResponse<?>> getAllDept() {
        ApiResponse<?> response = deptService.findAll();
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "获取部门")
    @PreAuthorize("@ss.hasPermi('system:menu:query')")
    @PostMapping("/getDept")
    public ResponseEntity<ApiResponse<?>> getDept(@Valid @RequestBody DeptDto deptDto) {
        ApiResponse<?> response = deptService.findDeptById(deptDto.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "新建部门")
    @PreAuthorize("@ss.hasPermi('system:menu:add')")
    @PostMapping("/createDept")
    public ResponseEntity<ApiResponse<?>> createDepartment(@RequestBody SysDept dept) {
        ApiResponse<?> response = deptService.createDept(dept);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "删除部门")
    @PreAuthorize("@ss.hasPermi('system:menu:remove')")
    @PostMapping("/deleteDept")
    public ResponseEntity<ApiResponse<?>> deleteDept(@RequestBody List<Long> deptIds) {
        ApiResponse<?> response = deptService.softDeleteDept(deptIds);
        return ResponseEntity.ok(response);
    }

    /**
     * 关闭部门
     */
    @Operation(summary = "关闭部门")
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @PostMapping("/enabledDept")
    public ResponseEntity<ApiResponse<?>> enabledDept(@RequestBody List<Long> deptIds) {
        ApiResponse<?> response = deptService.enabledDept(deptIds);
        return ResponseEntity.ok(response);
    }

    /**
     * 开启部门
     */
    @Operation(summary = "开启部门")
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @PostMapping("/unEnableDept")
    public ResponseEntity<ApiResponse<?>> unEnableDept(@RequestBody List<Long> deptIds) {
        ApiResponse<?> response = deptService.unEnabledDept(deptIds);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "修改部门")
    @PreAuthorize("@ss.hasPermi('system:menu:edit')")
    @PostMapping("/updateDept")
    public ResponseEntity<ApiResponse<?>> updateDept(@RequestBody SysDept dept) {
        ApiResponse<?> response = deptService.updateDept(dept);
        return ResponseEntity.ok(response);
    }
    /**
     * 部门新增岗位
     * TODO 部门新增岗位
     */

}
