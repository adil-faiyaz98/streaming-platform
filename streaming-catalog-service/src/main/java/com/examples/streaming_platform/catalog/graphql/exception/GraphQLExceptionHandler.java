package com.examples.streaming_platform.catalog.graphql.exception;

import com.examples.streaming_platform.catalog.graphql.exception.DgsEntityNotFoundException;
import graphql.GraphQLError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;

@Slf4j
@ControllerAdvice
public class GraphQLExceptionHandler {

    @GraphQlExceptionHandler
    public GraphQLError handleDgsEntityNotFoundException(DgsEntityNotFoundException ex) {
        log.warn("Entity not found: {}", ex.getMessage());
        return ex.toGraphQLError();
    }
}
