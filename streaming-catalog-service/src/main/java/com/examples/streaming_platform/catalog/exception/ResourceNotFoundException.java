// exception/ResourceNotFoundException.java
package com.examples.streaming_platform.catalog.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}