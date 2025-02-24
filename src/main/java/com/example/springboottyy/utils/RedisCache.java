package com.example.springboottyy.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @Author: Insight
 * @Description: redis 工具类
 * @Date: 2024/10/12 23:55
 * @Version: 1.0
 */
@Component
public class RedisCache {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
//    @Autowired
//    private RedisTemplate<Object,Object> redisTemplate;

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

}