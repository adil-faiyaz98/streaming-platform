package com.examples.streaming_platform.catalog.scalar;

import graphql.language.StringValue;
import graphql.schema.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@Configuration
@Profile("custom-datetime")  // Only activate when this profile is active
public class DateTimeScalar {

    private static final GraphQLScalarType DATE_TIME_SCALAR = GraphQLScalarType.newScalar()
            .name("DateTime")
            .description("A scalar type for DateTime")
            .coercing(new DateTimeCoercing())
            .build();

    @Bean
    public GraphQLScalarType dateTimeScalar() {
        return DATE_TIME_SCALAR;
    }

    private static class DateTimeCoercing implements Coercing<OffsetDateTime, String> {
        @Override
        public String serialize(Object dataFetcherResult) {
            if (dataFetcherResult instanceof OffsetDateTime) {
                return ((OffsetDateTime) dataFetcherResult).toString();
            }
            throw new CoercingSerializeException("Expected an OffsetDateTime object.");
        }

        @Override
        public OffsetDateTime parseValue(Object input) {
            if (input instanceof String) {
                try {
                    return OffsetDateTime.parse((String) input);
                } catch (DateTimeParseException e) {
                    throw new CoercingParseValueException("Invalid ISO-8601 format: " + input);
                }
            }
            throw new CoercingParseValueException("Expected a String");
        }

        @Override
        public OffsetDateTime parseLiteral(Object input) {
            if (input instanceof StringValue) {
                try {
                    return OffsetDateTime.parse(((StringValue) input).getValue());
                } catch (DateTimeParseException e) {
                    throw new CoercingParseLiteralException("Invalid ISO-8601 format: " + input);
                }
            }
            throw new CoercingParseLiteralException("Expected a StringValue");
        }
    }
}