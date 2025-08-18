package com.loopers.utils;

import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheCleanUp {

    private final CacheManager cacheManager;

    public CacheCleanUp(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    public void cleanUp() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
            }
        });
    }
}