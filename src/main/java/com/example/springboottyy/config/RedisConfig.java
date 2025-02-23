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
import org.springframework.data.redis.core.script.DefaultRedisScript;
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
    @Primary  //Primary 指定默认使用的bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = instanceConfig(3600 * 24 * 15L); //缓存30天
        return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).transactionAware().build();// transactionAware支持事务操作
    }

    @Bean
    public CacheManager cacheManager30m(RedisConnectionFactory connectionFactory) {
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

        FastJson2JsonRedisSerializer<Object> serializer = new FastJson2JsonRedisSerializer<>(Object.class);
        // 设置key 和 hash key 的序列化器为 String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(serializer);

        // 设置value 和 hash value 的序列化器为 String
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(serializer);

        // 这个方法可以不调用 本来就是一个回调方法用来确保redisTemplate初始化完成
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public DefaultRedisScript<Long> limitScript() {
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(limitScriptText());
        redisScript.setResultType(Long.class);
        return redisScript;
    }

    /**
     * 限流脚本
     */
    private String limitScriptText() {
        return "local key = KEYS[1]\n" +
                "local count = tonumber(ARGV[1])\n" +
                "local time = tonumber(ARGV[2])\n" +
                "local current = redis.call('get', key);\n" +
                "if current and tonumber(current) > count then\n" +
                "    return tonumber(current);\n" +
                "end\n" +
                "current = redis.call('incr', key)\n" +
                "if tonumber(current) == 1 then\n" +
                "    redis.call('expire', key, time)\n" +
                "end\n" +
                "return tonumber(current);";
    }
}
