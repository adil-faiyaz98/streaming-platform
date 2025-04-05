package com.examples.streaming_platform.catalog.monitoring;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for monitoring performance of service and repository methods.
 */
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class PerformanceMonitoringAspect {

    private final MetricService metricService;

    /**
     * Pointcut for all service methods.
     */
    @Pointcut("execution(* com.examples.streaming_platform.catalog.service.*.*(..))")
    public void serviceMethod() {
    }

    /**
     * Pointcut for all repository methods.
     */
    @Pointcut("execution(* com.examples.streaming_platform.catalog.repository.*.*(..))")
    public void repositoryMethod() {
    }

    /**
     * Advice to monitor service method performance.
     *
     * @param joinPoint the join point
     * @return the result of the method
     * @throws Throwable if an error occurs
     */
    @Around("serviceMethod()")
    public Object monitorServicePerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            log.debug("{}.{} executed in {}ms", className, methodName, executionTime);
            
            metricService.recordTime("service.method.duration", executionTime,
                    "class", className,
                    "method", methodName);
        }
    }

    /**
     * Advice to monitor repository method performance.
     *
     * @param joinPoint the join point
     * @return the result of the method
     * @throws Throwable if an error occurs
     */
    @Around("repositoryMethod()")
    public Object monitorRepositoryPerformance(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        
        try {
            return joinPoint.proceed();
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            
            // Only log slow queries to avoid excessive logging
            if (executionTime > 100) {
                log.warn("Slow database operation: {}.{} executed in {}ms", 
                        className, methodName, executionTime);
            }
            
            String operation = determineOperation(methodName);
            String entity = determineEntity(className);
            
            metricService.recordDatabaseOperation(operation, entity, executionTime);
        }
    }

    /**
     * Determine the database operation from the method name.
     *
     * @param methodName the method name
     * @return the operation type
     */
    private String determineOperation(String methodName) {
        if (methodName.startsWith("find") || methodName.startsWith("get") || methodName.startsWith("read")) {
            return "select";
        } else if (methodName.startsWith("save") || methodName.startsWith("insert")) {
            return "insert";
        } else if (methodName.startsWith("update")) {
            return "update";
        } else if (methodName.startsWith("delete") || methodName.startsWith("remove")) {
            return "delete";
        } else if (methodName.startsWith("count")) {
            return "count";
        } else {
            return "other";
        }
    }

    /**
     * Determine the entity type from the repository class name.
     *
     * @param className the class name
     * @return the entity type
     */
    private String determineEntity(String className) {
        if (className.endsWith("Repository")) {
            return className.substring(0, className.length() - "Repository".length()).toLowerCase();
        } else {
            return className.toLowerCase();
        }
    }
}
