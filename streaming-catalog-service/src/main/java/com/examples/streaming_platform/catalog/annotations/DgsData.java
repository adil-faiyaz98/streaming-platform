package com.examples.streaming_platform.catalog.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Custom annotation used to denote a DGS Data fetcher method.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface DgsData {

    String parentType();

    String field();
}
