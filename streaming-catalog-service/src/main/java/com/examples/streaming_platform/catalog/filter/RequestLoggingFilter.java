package com.examples.streaming_platform.catalog.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter to add request tracing and logging.
 */
@Component
@Slf4j
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final String TRACE_ID = "traceId";
    private static final String START_TIME = "startTime";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Generate trace ID and add to MDC for logging
        String traceId = UUID.randomUUID().toString();
        MDC.put(TRACE_ID, traceId);
        
        // Add trace ID to response headers
        response.addHeader("X-Trace-Id", traceId);
        
        // Record start time
        long startTime = System.currentTimeMillis();
        request.setAttribute(START_TIME, startTime);
        
        try {
            // Log incoming request
            log.info("Request: {} {} from {}", 
                    request.getMethod(), 
                    request.getRequestURI(),
                    request.getRemoteAddr());
            
            // Continue with the filter chain
            filterChain.doFilter(request, response);
        } finally {
            // Calculate request duration
            long duration = System.currentTimeMillis() - startTime;
            
            // Log completed request with status and duration
            log.info("Response: {} {} - {} in {}ms", 
                    request.getMethod(), 
                    request.getRequestURI(),
                    response.getStatus(),
                    duration);
            
            // Clean up MDC
            MDC.remove(TRACE_ID);
        }
    }
}
