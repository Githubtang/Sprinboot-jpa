package com.example.springboottyy.service.cache;

import org.springframework.cache.Cache;

import java.util.Set;

public interface CacheKeys {
    Set<String> getCacheKeys(final Cache cache);
}
