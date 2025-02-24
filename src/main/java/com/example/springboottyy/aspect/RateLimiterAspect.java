package com.example.springboottyy.aspect;

import com.example.springboottyy.annotation.RateLimiter;
import com.example.springboottyy.enums.LimitType;
import com.example.springboottyy.exception.ServiceException;
import com.example.springboottyy.utils.StringUtils;
import com.example.springboottyy.utils.ip.IpUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

/**
 * @Author: Insight
 * @Description: 限流处理
 * @Date: 2025/2/22 15:37
 * @Version: 1.0
 */
@Aspect
@Component
public class RateLimiterAspect {

    private static final Logger log = LoggerFactory.getLogger(RateLimiterAspect.class);

//    private RedisTemplate<String, Object> redisTemplate;
    private RedisTemplate<String, Object> redisTemplate;

    private RedisScript<Long> limitScript;

    @Autowired
    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setLimitScript(RedisScript<Long> limitScript) {
        this.limitScript = limitScript;
    }


    @Before("@annotation(rateLimiter)")
    public void doBefore(JoinPoint joinPoint, RateLimiter rateLimiter) {
        // 获取定义好的时间和次数
        int time = rateLimiter.time();
        int count = rateLimiter.count();

        // 获取合并后的 Redis 键
        String combineKey = getCombineKey(rateLimiter, joinPoint);
        List<String> keys = Collections.singletonList(combineKey);

        try {
            Long number = redisTemplate.execute(limitScript, keys, count, time);
//            Long number = redisTemplate.execute(limitScript, keys, String.valueOf(count), String.valueOf(time));
            if (StringUtils.isNull(number) || number.intValue() > count) {
                throw new ServiceException("访问过于频繁,请稍后再试");
            }
            log.info("限制请求'{}',当前请求'{}',缓存key'{}'", count, number.intValue(), combineKey);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw e;
//            throw new RuntimeException("服务器限流异常,请稍后再试");
        }
    }

    public String getCombineKey(RateLimiter rateLimiter, JoinPoint joinPoint) {
        // 从rateLimiter注解中获取 key
        StringBuffer stringBuffer = new StringBuffer(rateLimiter.key());

        // 如果限流类型基于IP,拼接客户端IP地址
        if (rateLimiter.limitType().equals(LimitType.IP)) {
            stringBuffer.append(IpUtils.getIpAddr()).append("-");
        }

        // 获取被调用方法的签名信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Class<?> targetClass = method.getDeclaringClass();

        // 拼接方法所在类的名称和方法名，确保每个方法的限流是独立的
        stringBuffer.append(targetClass.getName()).append("-").append(method.getName());
        return stringBuffer.toString();
    }
}
