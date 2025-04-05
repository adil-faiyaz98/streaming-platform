package com.examples.streaming_platform.catalog.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * Configuration for caching.
 */
@Configuration
@EnableCaching
public class CacheConfig {

    /**
     * Cache manager for local development.
     *
     * @return a simple in-memory cache manager
     */
    @Bean
    @Profile({"default", "test"})
    public CacheManager localCacheManager() {
        return new ConcurrentMapCacheManager(
                "movies", "series", "seasons", "episodes", "users");
    }

    /**
     * Redis cache manager for production.
     *
     * @param connectionFactory the Redis connection factory
     * @return a Redis cache manager
     */
    @Bean
    @Profile({"prod", "docker"})
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory) {
        // Configure serialization
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

        RedisSerializationContext.SerializationPair<Object> serializationPair =
                RedisSerializationContext.SerializationPair.fromSerializer(serializer);

        // Configure cache settings
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30)) // Cache entries expire after 30 minutes
                .serializeValuesWith(serializationPair)
                .prefixCacheNameWith("streaming-catalog::");

        // Create cache manager
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withCacheConfiguration("movies",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(10))
                                .serializeValuesWith(serializationPair)
                                .prefixCacheNameWith("streaming-catalog::"))
                .withCacheConfiguration("featured",
                        RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofHours(1))
                                .serializeValuesWith(serializationPair)
                                .prefixCacheNameWith("streaming-catalog::"))
                .build();
    }
}
