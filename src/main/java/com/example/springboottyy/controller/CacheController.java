package com.example.springboottyy.controller;

import com.example.springboottyy.common.constant.CacheConstants;
import com.example.springboottyy.common.core.text.Convert;
import com.example.springboottyy.system.domain.SysCache;
import com.example.springboottyy.utils.ApiResponse;
import com.example.springboottyy.utils.CacheUtils;
import com.example.springboottyy.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cache.Cache;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @Author: GithubTang
 * @Description: 缓存监控
 * @Date: 2025/3/5 20:03
 * @Version: 1.0
 */
@Tag(name = "缓存监控")
@RestController
@RequestMapping("/monitor/cache")
public class CacheController {
    private static String tmpCacheName = "";

    private final static List<SysCache> caches = new ArrayList<SysCache>();
    {
        caches.add(new SysCache(CacheConstants.LOGIN_TOKEN_KEY, "用户信息"));
        caches.add(new SysCache(CacheConstants.SYS_CONFIG_KEY, "配置信息"));
        caches.add(new SysCache(CacheConstants.SYS_DICT_KEY, "数据字典"));
        caches.add(new SysCache(CacheConstants.CAPTCHA_CODE_KEY, "验证码"));
        caches.add(new SysCache(CacheConstants.PHONE_CODES, "短信验证码"));
        caches.add(new SysCache(CacheConstants.EMAIL_CODES, "邮箱验证码"));
        caches.add(new SysCache(CacheConstants.REPEAT_SUBMIT_KEY, "防重提交"));
        caches.add(new SysCache(CacheConstants.RATE_LIMIT_KEY, "限流处理"));
        caches.add(new SysCache(CacheConstants.PWD_ERR_CNT_KEY, "密码错误次数"));
        caches.add(new SysCache(CacheConstants.IP_ERR_CNT_KEY, "IP错误次数"));
        caches.add(new SysCache(CacheConstants.FILE_MD5_PATH_KEY, "path-md5"));
        caches.add(new SysCache(CacheConstants.FILE_PATH_MD5_KEY, "md5-path"));
    }

    @Operation(summary = "获取缓存名列表")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @PostMapping("/getNames")
    public ApiResponse<?> cache(){
        ApiResponse<List<SysCache>> response = ApiResponse.success("查询成功", caches);
        return response;
    }

    @Operation(summary = "获取缓存键列表")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @Parameters({
        @Parameter(name = "cacheName",description = "缓存名称",required = true)
    })
    @PostMapping("/getKeys")
    public ApiResponse<?> getCacheKeys(@RequestBody String cacheName){
        tmpCacheName = cacheName;
        Set<String> keys = CacheUtils.getKeys(cacheName);
        return ApiResponse.success("查询成功",keys);
    }

    @Operation(summary = "获取缓存值列表")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @Parameters({
            @Parameter(name = "cacheName",description = "缓存名称",required = true),
            @Parameter(name = "cacheKey",description = "缓存键名",required = true)
    })
    @PostMapping("/getValue")
    public ApiResponse<?> getCacheValue(@RequestParam String cacheName, @RequestParam String cacheKey){
        Cache.ValueWrapper valueWrapper = CacheUtils.get(cacheName, cacheKey);
        SysCache sysCache = new SysCache();
        sysCache.setCacheName(cacheName);
        sysCache.setCacheKey(cacheKey);
        if (StringUtils.isNotNull(valueWrapper)){
            sysCache.setCacheValue(Convert.toStr(valueWrapper.get(),""));
        }
        return ApiResponse.success("查询成功",sysCache);
    }

    @Operation(summary = "清除缓存")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @Parameters({
            @Parameter(name = "cacheName",description = "缓存名称",required = true)
    })
    @PostMapping("/clearCacheName")
    public ApiResponse<?> clearCacheName(@RequestBody String cacheName){
        CacheUtils.clear(cacheName);
        return ApiResponse.success();
    }

    @Operation(summary = "清除缓存值")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @Parameters({
            @Parameter(name = "cacheKey",description = "缓存键名",required = true)
    })
    @PostMapping("/clearCacheKey")
    public ApiResponse<?> clearCacheKey(@RequestBody String cacheKey){
        CacheUtils.removeIfPresent(tmpCacheName, cacheKey);
        return ApiResponse.success();
    }

    @Operation(summary = "清除所有缓存")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @PostMapping("/clearCacheAll")
    public ApiResponse<?> clearCacheAll(){
        Collection<String> cacheNames = CacheUtils.getCacheManager().getCacheNames();
        for (String cacheName : cacheNames){
            CacheUtils.clear(cacheName);
        }
        return ApiResponse.success();
    }
}
