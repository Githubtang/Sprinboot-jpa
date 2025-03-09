package com.example.springboottyy.config;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.example.springboottyy.common.constant.Constants;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @Author: Insight
 * @Description: Redis使用FastJson序列化
 * @Date: 2025/2/20 17:45
 * @Version: 1.0
 */
public class FastJson2JsonRedisSerializer<T> implements RedisSerializer<T> {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter(Constants.JSON_WHITELIST_STR);

    private final Class<T> clazz;

    public FastJson2JsonRedisSerializer(Class<T> clazz) {
        super();
        this.clazz = clazz;
    }

    @Override
    public byte[] serialize(T t) throws SerializationException {
        if (t == null) {
            return new byte[0];
        }
        try {
            return JSON.toJSONBytes(t, JSONWriter.Feature.WriteClassName,
                    JSONWriter.Feature.WriteMapNullValue,
                    JSONWriter.Feature.WriteBigDecimalAsPlain);
        } catch (Exception ex) {
            throw new SerializationException("Could not serialize: " + ex.getMessage(), ex);
        }
    }

    @Override
    public T deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }
        try {
            return JSON.parseObject(bytes, clazz, AUTO_TYPE_FILTER,
                    JSONReader.Feature.SupportAutoType,
                    JSONReader.Feature.IgnoreAutoTypeNotMatch);
        } catch (Exception ex) {
            throw new SerializationException("Could not deserialize: " + ex.getMessage(), ex);
        }
    }
}