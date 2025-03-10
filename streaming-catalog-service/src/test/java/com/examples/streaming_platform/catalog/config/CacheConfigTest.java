package com.examples.streaming_platform.catalog.config;

import org.junit.jupiter.api.Test;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class CacheConfigTest {

    @Test
    void cacheManagerBean_ShouldBeCreatedAndNotNull() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CacheConfig.class);
        CacheManager cacheManager = context.getBean(CacheManager.class);
        assertNotNull(cacheManager, "CacheManager should not be null");
        context.close();
    }
}
