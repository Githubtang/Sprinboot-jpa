package com.example.springboottyy.service.cache;

import java.util.concurrent.TimeUnit;

public interface CacheTimeOut extends CacheNoTimeOut{
    <T> void setCacheObject(final String cacheName, final String key,final T value,
                            final long timeout,final TimeUnit timeUnit);
}
