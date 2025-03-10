package com.examples.streaming_platform.catalog.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * A generic error response used for most exceptions.
 */
@Data
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private int status;
    private String message;
    private LocalDateTime timestamp;

    // Optional: If you need a no-argument constructor
    public ErrorResponse() {
        this.status = 404;
        this.message = "Entity not found";
        this.timestamp = LocalDateTime.now();
    }
}
