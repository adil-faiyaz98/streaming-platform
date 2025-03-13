package com.examples.streaming_platform.catalog.scalar;

import com.examples.streaming_platform.catalog.annotations.DgsScalar;
import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.GraphQLScalarType;

import java.time.Instant;

@DgsScalar(name = "DateTime")
public class DateTimeScalar implements Coercing<Instant, String> {

    @Override
    public String serialize(Object dataFetcherResult) {
        if (dataFetcherResult instanceof Instant) {
            return ((Instant) dataFetcherResult).toString();
        }
        throw new IllegalArgumentException("Invalid value for DateTime serialization: " + dataFetcherResult);
    }

    @Override
    public Instant parseValue(Object input) {
        if (input instanceof String) {
            return Instant.parse((String) input);
        }
        throw new IllegalArgumentException("Invalid value for DateTime parsing: " + input);
    }

    @Override
    public Instant parseLiteral(Object input) {
        if (input instanceof StringValue) {
            return Instant.parse(((StringValue) input).getValue());
        }
        throw new IllegalArgumentException("Invalid literal value for DateTime: " + input);
    }

    public static final GraphQLScalarType DATE_TIME = GraphQLScalarType.newScalar()
            .name("DateTime")
            .description("A scalar type for DateTime values using ISO-8601 format")
            .coercing(new DateTimeScalar())
            .build();
}
