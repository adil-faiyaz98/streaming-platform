package com.examples.streaming_platform.catalog.graphql.exception;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import org.springframework.graphql.execution.ErrorType;

public class DgsEntityNotFoundException extends RuntimeException {

    public DgsEntityNotFoundException(String message) {
        super(message);
    }

    public GraphQLError toGraphQLError() {
        return GraphqlErrorBuilder.newError()
                .message(getMessage())
                .errorType(ErrorType.NOT_FOUND)
                .build();
    }
}
