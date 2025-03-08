package com.examples.streaming_platform.catalog.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> {
            builder
                    .withCacheConfiguration("movieCache",
                            RedisCacheConfiguration.defaultCacheConfig()
                                    .entryTtl(Duration.ofMinutes(10)))
                    .withCacheConfiguration("seriesCache",
                            RedisCacheConfiguration.defaultCacheConfig()
                                    .entryTtl(Duration.ofMinutes(10)))
                    .withCacheConfiguration("topRatedCache",
                            RedisCacheConfiguration.defaultCacheConfig()
                                    .entryTtl(Duration.ofMinutes(5)))
                    .withCacheConfiguration("featuredCache",
                            RedisCacheConfiguration.defaultCacheConfig()
                                    .entryTtl(Duration.ofMinutes(30)));
        };
    }
}