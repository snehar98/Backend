package com.github.sneha.springboot_redis_caching.advice;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Custom annotation to log the execution time of methods or classes.
 * Can be applied to methods or entire classes to track the duration
 * of their execution.
 *
 * Usage:
 * - Apply "@LogExecutionTime" annotation to a method or class to measure and log execution time.
 *
 * @author sneharavikumartl
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface LogExecutionTime {
    String value() default ""; // Optional description
}
