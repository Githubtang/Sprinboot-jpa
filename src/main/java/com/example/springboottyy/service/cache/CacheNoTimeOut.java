package com.example.springboottyy.service.cache;

public interface CacheNoTimeOut {
    <T> void setCacheObject(final String name, final String key, final T value);
}
