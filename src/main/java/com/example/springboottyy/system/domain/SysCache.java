package com.example.springboottyy.system.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: GithubTang
 * @Description: 缓存信息
 * @Date: 2025/3/5 20:08
 * @Version: 1.0
 */
@Schema(title = "缓存信息")
@Data
public class SysCache {
    /** 缓存名称 */
    @Schema(title = "缓存名称")
    private String cacheName = "";

    /** 缓存键名 */
    @Schema(title = "缓存键名")
    private String cacheKey = "";

    /** 缓存内容 */
    @Schema(title = "缓存内容")
    private String cacheValue = "";

    /** 备注 */
    @Schema(title = "备注")
    private String remake = "";

    public SysCache() {
    }

    public SysCache(String remake, String cacheName) {
        this.remake = remake;
        this.cacheName = cacheName;
    }

    public SysCache(String cacheName, String cacheKey, String cacheValue) {
        this.cacheName = cacheName;
        this.cacheKey = cacheKey;
        this.cacheValue = cacheValue;
    }
}
