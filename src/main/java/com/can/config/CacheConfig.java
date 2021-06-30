package com.can.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;


public class CacheConfig extends CachingConfigurerSupport {
    public static final String PROPERTY_RESOLVING_CACHE_RESOLVER_BEAN_NAME = "propertyResolvingCacheResolver";

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    Environment environment;

    @Bean(PROPERTY_RESOLVING_CACHE_RESOLVER_BEAN_NAME)
    @Override
    public CacheResolver cacheResolver() {
        return new CanResolvingCacheResolver(cacheManager, environment);
    }
}
