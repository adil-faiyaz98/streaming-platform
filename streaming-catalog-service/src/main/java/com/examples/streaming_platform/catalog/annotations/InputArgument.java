package com.examples.streaming_platform.catalog.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates an argument that should be extracted from the GraphQL request.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface InputArgument {
    String value() default "";
}