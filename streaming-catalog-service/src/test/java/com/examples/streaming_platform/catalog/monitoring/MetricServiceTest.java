package com.examples.streaming_platform.catalog.monitoring;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricServiceTest {

    @Mock
    private MeterRegistry meterRegistry;

    @Mock
    private Counter counter;

    @Mock
    private Timer timer;

    @Mock
    private Counter.Builder counterBuilder;

    @Mock
    private Timer.Builder timerBuilder;

    @Captor
    private ArgumentCaptor<String> nameCaptor;

    @Captor
    private ArgumentCaptor<String[]> tagsCaptor;

    private MetricService metricService;

    @BeforeEach
    void setUp() {
        metricService = new MetricService(meterRegistry);
    }

    @Test
    void incrementCounter_ShouldRegisterAndIncrementCounter() {
        // Given
        when(meterRegistry.counter(anyString(), any(String[].class))).thenReturn(counter);

        // When
        metricService.incrementCounter("test.counter", "tag1", "value1");

        // Then
        verify(meterRegistry).counter(eq("test.counter"), eq(new String[]{"tag1", "value1"}));
        verify(counter).increment();
    }

    @Test
    void recordApiCall_ShouldRecordTimeAndIncrementCounter() {
        // Given
        SimpleMeterRegistry registry = new SimpleMeterRegistry();
        MetricService service = new MetricService(registry);

        // When
        service.recordApiCall("/api/test", "GET", 200, 150);

        // Then
        assertEquals(1, registry.find("api.request.count")
                .tag("endpoint", "/api/test")
                .tag("method", "GET")
                .tag("status", "200")
                .tag("status_group", "2xx")
                .counter().count());

        assertEquals(1, registry.find("api.request.duration")
                .tag("endpoint", "/api/test")
                .tag("method", "GET")
                .tag("status", "200")
                .tag("status_group", "2xx")
                .timer().count());
    }
}
