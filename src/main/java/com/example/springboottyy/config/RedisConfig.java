package com.example.springboottyy.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * @Author: Insight
 * @Description: Redis配置类
 * @Date: 2024/10/12 23:59
 * @Version: 1.0
 */
@Configuration
public class RedisConfig implements CachingConfigurer {

    @Bean
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = instanceConfig(3600 * 24 * 15L); //缓存30天
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).transactionAware().build();// transactionAware支持事务操作
    }

    @Bean
    public CacheManager cacheManager30m(RedisConnectionFactory connectionFactory){
        RedisCacheConfiguration config = instanceConfig(1800L);
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).transactionAware().build();
    }

    @SuppressWarnings(value = {"unchecked", "rawtypes"})
    private RedisCacheConfiguration instanceConfig(Long ttl) {
        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);
        return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(ttl)) //缓存时间以秒计算
                .disableCachingNullValues()
                .computePrefixWith(name -> name + ":")
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 设置key 和 hash key 的序列化器为 String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // 设置value 和 hash value 的序列化器为 String
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());
        return redisTemplate;
    }
}
