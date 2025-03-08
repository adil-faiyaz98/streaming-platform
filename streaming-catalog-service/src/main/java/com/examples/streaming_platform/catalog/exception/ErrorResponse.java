// exception/ErrorResponse.java
package com.examples.streaming_platform.catalog.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
}