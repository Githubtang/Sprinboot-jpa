package com.example.springboottyy.utils;

import com.example.springboottyy.service.cache.CacheKeys;
import com.example.springboottyy.service.cache.CacheTimeOut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Insight
 * @Description: redis 工具类
 * @Date: 2024/10/12 23:55
 * @Version: 1.0
 */
@Component
public class RedisCache implements CacheKeys, CacheTimeOut {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 保存token 过期时间
    public void setToken(String token, String username, long timeout) {
        redisTemplate.opsForValue().set(token, username, timeout, TimeUnit.SECONDS);
    }

    // 获取token对应的username
    public String getUsernameFromToken(String token) {
        return (String) redisTemplate.opsForValue().get(token);
    }

    // 删除token
    public void delToken(String token) {
        redisTemplate.delete(token);
    }

    // 判断token是否存在
    public boolean hasToken(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(token));
    }

    @Override
    public Set<String> getCacheKeys(Cache cache) {
        Set<String> keySet = new HashSet<>();
        Set<String> keySets = redisTemplate.keys(cache.getName() + "*");
        for (String key : keySets) {
            keySet.add(StringUtils.replace(key, cache.getName() + ":", ""));
        }
        return keySet;
    }


    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key   缓存的键值
     * @param value 缓存的值
     */
    @Override
    public <T> void setCacheObject(final String cacheName, final String key, T value) {
        redisTemplate.opsForValue().set(cacheName + ":" + key, value);
    }

    /**
     * 缓存基本的对象，Integer、String、实体类等
     *
     * @param key      缓存的键值
     * @param value    缓存的值
     * @param timeout  时间
     * @param timeUnit 时间颗粒度
     */
    @Override
    public <T> void setCacheObject(String cacheName, String key, T value, long timeout, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(cacheName + ":" + key, value, timeout, timeUnit);
    }
}