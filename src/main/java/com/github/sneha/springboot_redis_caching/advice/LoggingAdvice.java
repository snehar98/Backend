package com.github.sneha.springboot_redis_caching.advice;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging the execution time of methods annotated with @LogExecutionTime.
 * This class uses Aspect-Oriented Programming (AOP) to intercept method calls,
 * measure their execution time, and log the results.
 *
 * It uses the @Around advice to wrap the method execution, calculating the time
 * taken to execute the method and logging the metric.
 *
 * @author sneharavikumartl
 */

@Aspect
@Component
@Slf4j
public class LoggingAdvice {

    @Around("@annotation(LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("[Execution Time Metric] - Method - {}  Execution time - {} ms", joinPoint.getSignature(), executionTime);
        return proceed;
    }
}
