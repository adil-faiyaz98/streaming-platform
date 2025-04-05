package com.examples.streaming_platform.catalog.config;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Configuration for resilience patterns including circuit breakers, retries, and time limiters.
 */
@Configuration
public class ResilienceConfig {

    /**
     * Configure the circuit breaker registry with default settings.
     *
     * @return the circuit breaker registry
     */
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig.custom()
                .failureRateThreshold(50)                 // 50% failure rate to open circuit
                .waitDurationInOpenState(Duration.ofSeconds(10)) // Wait 10 seconds before trying again
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(10)                    // Consider last 10 calls for failure rate
                .minimumNumberOfCalls(5)                  // Minimum calls before calculating failure rate
                .permittedNumberOfCallsInHalfOpenState(3) // Allow 3 calls in half-open state
                .automaticTransitionFromOpenToHalfOpenEnabled(true)
                .build();

        return CircuitBreakerRegistry.of(circuitBreakerConfig);
    }

    /**
     * Configure the retry registry with default settings.
     *
     * @return the retry registry
     */
    @Bean
    public RetryRegistry retryRegistry() {
        RetryConfig retryConfig = RetryConfig.custom()
                .maxAttempts(3)                           // Maximum 3 retry attempts
                .waitDuration(Duration.ofMillis(500))     // Wait 500ms between retries
                .retryExceptions(Exception.class)         // Retry on all exceptions
                .build();

        return RetryRegistry.of(retryConfig);
    }

    /**
     * Configure the time limiter registry with default settings.
     *
     * @return the time limiter registry
     */
    @Bean
    public TimeLimiterRegistry timeLimiterRegistry() {
        TimeLimiterConfig timeLimiterConfig = TimeLimiterConfig.custom()
                .timeoutDuration(Duration.ofSeconds(5))   // 5 second timeout
                .cancelRunningFuture(true)                // Cancel running future on timeout
                .build();

        return TimeLimiterRegistry.of(timeLimiterConfig);
    }
}
