package com.example.springboottyy.controller;

import com.example.springboottyy.model.SysNotice;
import com.example.springboottyy.service.SysNoticeService;
import com.example.springboottyy.utils.ApiResponse;
import com.example.springboottyy.utils.SecurityUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: GithubTang
 * @Description: 公告 信息操作处理
 * @Date: 2025/3/16 20:35
 * @Version: 1.0
 */
@Tag(name = "公告",description = "信息操作处理")
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController {

    @Autowired
    private SysNoticeService sysNoticeService;
    /**
     * 获取通知公告列表
     */
    @Operation(summary = "获取通知公告列表")
    @PreAuthorize("@ss.hasPermi('system:notice:list')")
    @PostMapping("/list")
    public ApiResponse<?> list(SysNotice notice)
    {
        return sysNoticeService.selectNoticeList(notice);
    }

    /**
     * 根据通知公告编号获取详细信息
     */
    @Operation(summary = "根据通知公告编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:notice:query')")
    @PostMapping(value = "/findById")
    public ApiResponse<?> getInfo(@RequestBody Long noticeId)
    {
        return sysNoticeService.selectNoticeById(noticeId);
    }

    /**
     * 新增通知公告
     */
    @Operation(summary = "新增通知公告")
    @PreAuthorize("@ss.hasPermi('system:notice:add')")
    @PostMapping
    public ApiResponse<?> add(@Validated @RequestBody SysNotice notice)
    {
        notice.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        return sysNoticeService.insertNotice(notice);
    }

    /**
     * 修改通知公告
     */
    @Operation(summary = "修改通知公告")
    @PreAuthorize("@ss.hasPermi('system:notice:edit')")
    @PutMapping
    public ApiResponse<?> edit(@Validated @RequestBody SysNotice notice)
    {
        notice.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        return sysNoticeService.updateNotice(notice);
    }

    /**
     * 删除通知公告
     */
    @Operation(summary = "删除通知公告")
    @PreAuthorize("@ss.hasPermi('system:notice:remove')")
    @DeleteMapping("/delIds")
    public ApiResponse<?> remove(@RequestBody List<Long> noticeIds)
    {
        return sysNoticeService.deleteNoticeByIds(noticeIds);
    }

}
