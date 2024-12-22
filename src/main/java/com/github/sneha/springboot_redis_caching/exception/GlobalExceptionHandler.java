package com.github.sneha.springboot_redis_caching.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

/**
 * GlobalExceptionHandler handles all exceptions thrown in the application.
 * It provides specific handling for validation errors, database integrity errors, and unexpected errors.
 * The class uses Spring's @RestControllerAdvice to globally handle exceptions across all controllers.
 *
 * @author sneharavikumartl
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles MethodArgumentNotValidException, which occurs when request validation fails.
     * This typically happens when a request body is invalid, and fields do not meet the validation constraints.
     *
     * @param ex the exception object
     * @return a ResponseEntity containing the error messages with a 400 Bad Request status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<Object> handleValidationException(MethodArgumentNotValidException ex) {
        // Collect all validation error messages into a list
        List<String> errorMessages = ex.getBindingResult().getAllErrors().stream()
                .map(error -> ((FieldError) error).getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        // Return a 400 Bad Request response with the error messages
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessages);
    }

    /**
     * Handles DataIntegrityViolationException, which occurs when a database constraint is violated.
     * This is typically triggered by issues like duplicate entries or foreign key violations.
     *
     * @param ex the exception object
     * @return a ResponseEntity containing the error message with a 400 Bad Request status
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        // Return a 400 Bad Request response with the cause of the database error
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Database error occurred: " + ex.getCause().getMessage());
    }

    /**
     * Handles all other unexpected exceptions. This is a catch-all handler for any exceptions
     * that are not specifically handled by the above methods.
     *
     * @param ex the exception object
     * @return a ResponseEntity containing the error message with a 500 Internal Server Error status
     */
//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
//        ex.printStackTrace();;
//        // Return a 500 Internal Server Error response with the unexpected error message
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
//    }

    @ExceptionHandler(UserDetailsNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Object> handleUserNotFound(UserDetailsNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
