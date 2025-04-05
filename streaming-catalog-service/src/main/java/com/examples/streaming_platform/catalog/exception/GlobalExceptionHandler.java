package com.examples.streaming_platform.catalog.exception;

import com.examples.streaming_platform.catalog.graphql.exception.DgsEntityNotFoundException;
import com.examples.streaming_platform.catalog.graphql.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized exception handling for the entire application.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handle JPA EntityNotFoundExceptions, custom DgsEntityNotFoundException, and ResourceNotFoundException exceptions.
     *
     * @param ex the thrown exception
     * @return a standardized ErrorResponse with status 404
     */
    @ExceptionHandler({EntityNotFoundException.class, DgsEntityNotFoundException.class, ResourceNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundExceptions(RuntimeException ex) {
        log.warn("Not found exception: {}", ex.getMessage());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    /**
     * Handles Bean Validation errors from @Valid annotated request bodies.
     *
     * @param ex the thrown MethodArgumentNotValidException
     * @return a ValidationErrorResponse containing field-level errors
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        log.warn("Validation failed: {}", errors);

        ValidationErrorResponse validationError = ValidationErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed")
                .errors(errors)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.badRequest().body(validationError);
    }

    /**
     * Handle security-related exceptions.
     *
     * @param ex the thrown exception
     * @return a standardized ErrorResponse with status 401 or 403
     */
    @ExceptionHandler({
            org.springframework.security.access.AccessDeniedException.class,
            org.springframework.security.authentication.BadCredentialsException.class,
            org.springframework.security.authentication.InsufficientAuthenticationException.class
    })
    public ResponseEntity<ErrorResponse> handleSecurityExceptions(Exception ex) {
        log.warn("Security exception: {}", ex.getMessage());

        HttpStatus status = ex instanceof org.springframework.security.access.AccessDeniedException ?
                HttpStatus.FORBIDDEN : HttpStatus.UNAUTHORIZED;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(status.value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(status)
                .body(errorResponse);
    }

    /**
     * Handle data integrity violations (e.g., unique constraint violations).
     *
     * @param ex the thrown exception
     * @return a standardized ErrorResponse with status 409
     */
    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(org.springframework.dao.DataIntegrityViolationException ex) {
        log.warn("Data integrity violation: {}", ex.getMessage());

        String message = "Data integrity violation";
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            message = "A record with the same unique identifier already exists";
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(errorResponse);
    }

    /**
     * Fallback for any other unhandled exception types.
     *
     * @param ex the thrown exception
     * @return a generic ErrorResponse with status 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex) {
        log.error("Unexpected error occurred", ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred. Please contact support.")
                .timestamp(LocalDateTime.now())
                .traceId(java.util.UUID.randomUUID().toString()) // Add trace ID for debugging
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
