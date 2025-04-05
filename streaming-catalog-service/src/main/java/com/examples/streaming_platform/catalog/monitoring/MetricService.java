package com.examples.streaming_platform.catalog.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Service for recording application metrics.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class MetricService {

    private final MeterRegistry meterRegistry;

    /**
     * Increment a counter metric.
     *
     * @param name the metric name
     * @param tags the metric tags
     */
    public void incrementCounter(String name, String... tags) {
        Counter counter = Counter.builder(name)
                .tags(tags)
                .description("Counter for " + name)
                .register(meterRegistry);
        counter.increment();
    }

    /**
     * Record a timing metric.
     *
     * @param name the metric name
     * @param timeInMs the time in milliseconds
     * @param tags the metric tags
     */
    public void recordTime(String name, long timeInMs, String... tags) {
        Timer timer = Timer.builder(name)
                .tags(tags)
                .description("Timer for " + name)
                .register(meterRegistry);
        timer.record(timeInMs, TimeUnit.MILLISECONDS);
    }

    /**
     * Record API call metrics.
     *
     * @param endpoint the API endpoint
     * @param method the HTTP method
     * @param status the HTTP status code
     * @param timeInMs the time in milliseconds
     */
    public void recordApiCall(String endpoint, String method, int status, long timeInMs) {
        String statusGroup = status / 100 + "xx";
        recordTime("api.request.duration", timeInMs,
                "endpoint", endpoint,
                "method", method,
                "status", String.valueOf(status),
                "status_group", statusGroup);
        
        incrementCounter("api.request.count",
                "endpoint", endpoint,
                "method", method,
                "status", String.valueOf(status),
                "status_group", statusGroup);
    }

    /**
     * Record database operation metrics.
     *
     * @param operation the database operation (e.g., "select", "insert")
     * @param entity the entity type
     * @param timeInMs the time in milliseconds
     */
    public void recordDatabaseOperation(String operation, String entity, long timeInMs) {
        recordTime("db.operation.duration", timeInMs,
                "operation", operation,
                "entity", entity);
        
        incrementCounter("db.operation.count",
                "operation", operation,
                "entity", entity);
    }
}
