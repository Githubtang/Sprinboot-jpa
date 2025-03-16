package com.example.springboottyy.controller;

import com.example.springboottyy.model.SysOperLog;
import com.example.springboottyy.service.SysOperLogService;
import com.example.springboottyy.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @Author: GithubTang
 * @Description: 操作日志记录
 * @Date: 2025/3/15 16:14
 * @Version: 1.0
 */
@Tag(name = "操作日志")
@Controller
@RequestMapping("/monitor/operlog")
public class SysOperlogController {
    @Autowired
    private SysOperLogService operLogService;

    @Operation(summary = "获取操作日志记录列表")
    @PreAuthorize("@ss.hasPermi('monitor:operlog:list')")
    @PostMapping("/list")
    public ApiResponse<?> list(SysOperLog sysOperLog) {
        return operLogService.selectOperLogList(sysOperLog);
    }

    @Operation(summary = "删除操作日志记录")
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remover')")
    @PostMapping("/delByIds")
    public ApiResponse<?> remove(@RequestBody List<Long> ids) {
        operLogService.deleteOperLogByIds(ids);
        return ApiResponse.success();
    }

    @Operation(summary = "清除操作日志记录")
    @PreAuthorize("@ss.hasPermi('monitor:operlog:remover')")
    @PostMapping("/clean")
    public ApiResponse<?> clean() {
        operLogService.cleanOperLog();
        return ApiResponse.success();
    }
}
