package com.example.springboottyy.controller;

import com.example.springboottyy.annotation.RateLimiter;
import com.example.springboottyy.enums.LimitType;
import com.example.springboottyy.utils.ApiResponse;
import com.example.springboottyy.utils.StringUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @Author: Insight
 * @Description: 缓存监控
 * @Date: 2025/2/23 14:58
 * @Version: 1.0
 */
@Tag(name = "缓存监控")
@RestController
@RequestMapping("/monitor/cache")
public class RedisCacheController {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Operation(summary = "获取缓存信息")
    @PreAuthorize("@ss.hasPermi('monitor:cache:list')")
    @RateLimiter(limitType = LimitType.IP)
    @RequestMapping()
    public ResponseEntity<ApiResponse<?>> getInfo() {
        HashMap<String, Object> result = new HashMap<>();
        Properties info = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.commands().info());
        Properties commandStats = (Properties) redisTemplate.execute((RedisCallback<Object>) connection -> connection.commands().info("commandstats"));
        Object dbSize = redisTemplate.execute((RedisCallback<Object>) connection -> connection.commands().dbSize());
        result.put("info", info);
        result.put("commandStats", commandStats);
        result.put("dbSize", dbSize);

        List<Map<String, String>> pieList = new ArrayList<>();
        commandStats.stringPropertyNames().forEach(key -> {
            HashMap<String, String> data = new HashMap<>(2);
            String property = commandStats.getProperty(key);
            data.put("name", StringUtils.removeStart(key, "cmdstat_"));
            data.put("value", StringUtils.substringBetween(property, "calls=", ",usec"));
            pieList.add(data);
        });
        result.put("pieList", pieList);
        ApiResponse<?> response = new ApiResponse<>("200", "success", result);
        return ResponseEntity.ok(response);
    }
}
