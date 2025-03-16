package com.example.springboottyy.controller;

import com.example.springboottyy.model.SysConfig;
import com.example.springboottyy.service.impl.SysConfigService;
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
 * @Description: 参数配置 信息操作处理
 * @Date: 2025/3/13 23:13
 * @Version: 1.0
 */
@Tag(name = "参数配置")
@RestController
@RequestMapping("/config")
public class SysConfigController {
    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 获取参数配置列表
     */
    @Operation(summary = "获取参数配置列表")
    @PreAuthorize("@ss.hasPermi('system:config:list')")
    @RequestMapping("/findAll")
    public ApiResponse<?> findAll(@RequestBody SysConfig sysConfig) {
        List<SysConfig> configs = sysConfigService.selectConfigList(sysConfig);
        return ApiResponse.success(configs);
    }

    /**
     * 根据参数编号获取详细信息
     */
    @Operation(summary = "根据参数编号获取详细信息")
    @PreAuthorize("@ss.hasPermi('system:config:query')")
    @RequestMapping("/findById")
    public ApiResponse<?> findById(@RequestBody Long configId) {
        SysConfig sysConfig = sysConfigService.selectConfigById(configId);
        return ApiResponse.success(sysConfig);
    }

    /**
     * 根据参数键名查询参数值
     */
    @Operation(summary = "根据参数键名查询参数值")
    @GetMapping(value = "/configId")
    public ApiResponse<?> getInfo(@RequestBody Long configId) {
        return ApiResponse.success(sysConfigService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     */
    @Operation(summary = "根据参数键名查询参数值")
    @GetMapping(value = "/configKey")
    public ApiResponse<?> getConfigKey(@RequestBody String configKey)
    {
        return ApiResponse.success(sysConfigService.selectConfigByKey(configKey));
    }


    /**
     * 根据参数编号获取详细信息
     */
    @Operation(summary = "新增参数配置")
    @PreAuthorize("@ss.hasPermi('system:config:add')")
    @PostMapping()
    public ApiResponse<?> add(@RequestBody SysConfig sysConfig) {
        if (!sysConfigService.checkConfigKeyUnique(sysConfig)) {
            return ApiResponse.error("新增参数" + sysConfig.getConfigName() + "失败,参数已存在");
        }
        sysConfig.setCreateBy(SecurityUtils.getLoginUser().getUsername());
        int i = sysConfigService.insertConfig(sysConfig);
        return ApiResponse.success(i);
    }

    /**
     * 修改参数配置
     */
    @Operation(summary = "修改参数配置")
    @PreAuthorize("@ss.hasPermi('system:config:edit')")
    @PutMapping
    public ApiResponse<?> edit(@Validated @RequestBody SysConfig config) {
        if (!sysConfigService.checkConfigKeyUnique(config)) {
            return ApiResponse.error("修改参数'" + config.getConfigName() + "'失败，参数键名已存在");
        }
        config.setUpdateBy(SecurityUtils.getLoginUser().getUsername());
        return ApiResponse.success(sysConfigService.updateConfig(config));
    }

    /**
     * 删除参数配置
     */
    @Operation(summary = "删除参数配置")
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/configIds")
    public ApiResponse<?> remove(@RequestBody List<Long> configIds) {
        sysConfigService.deleteConfigByIds(configIds);
        return ApiResponse.success();
    }

    /**
     * 刷新参数缓存
     */
    @Operation(summary = "刷新参数缓存")
    @PreAuthorize("@ss.hasPermi('system:config:remove')")
    @DeleteMapping("/refreshCache")
    public ApiResponse<?> refreshCache() {
        sysConfigService.resetConfigCache();
        return ApiResponse.success();
    }
}
