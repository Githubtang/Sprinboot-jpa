package com.example.springboottyy.controller;

import com.example.springboottyy.model.SysLogininfor;
import com.example.springboottyy.service.SysLogininforService;
import com.example.springboottyy.service.SysPasswordService;
import com.example.springboottyy.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: GithubTang
 * @Description: 系统访问记录
 * @Date: 2025/3/16 16:36
 * @Version: 1.0
 */
@Tag(name = "系统访问记录")
@RestController
@RequestMapping("/monitor/logininfor")
public class SysLogininforController {
    @Autowired
    private SysLogininforService logininforService;

    @Autowired
    private SysPasswordService passwordService;

    @Operation(summary = "获取系统访问记录列表")
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:list')")
    @PostMapping("/list")
    public ApiResponse<?> list(@RequestBody SysLogininfor logininfor) {
        return logininforService.selectLogininforList(logininfor);
    }

    @Operation(summary = "删除系统访问记录")
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @PostMapping("/remove")
    public ApiResponse<?> remove(@RequestBody List<Long> ids) {
        return logininforService.deleteLogininfor(ids);
    }

    @Operation(summary = "清除系统访问记录")
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:remove')")
    @PostMapping("/clean")
    public ApiResponse<?> clean() {
        return logininforService.cleanLogininfor();
    }

    @Operation(summary = "账户解锁")
    @Parameters({
            @Parameter(name = "userName", description = "用户名", required = true),
    })
    @PreAuthorize("@ss.hasPermi('monitor:logininfor:unlock')")
    @PostMapping("/unlock")
    public ApiResponse<?> unlock(@RequestBody String userName) {
        passwordService.clearLoginRecordCache(userName);
        return ApiResponse.success();
    }
}
