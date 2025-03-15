package com.examples.streaming_platform.catalog.config;

import graphql.scalars.ExtendedScalars;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

/**
 * Configures additional custom scalars for GraphQL if needed.
 */
@Configuration
@Slf4j
public class ScalarConfig {

    /**
     * Wires in ExtendedScalars.DateTime for GraphQL queries.
     *
     * @return a RuntimeWiringConfigurer bean
     */
    @Bean
    public RuntimeWiringConfigurer dateTimeScalarConfigurer() {  // Renamed method
        return builder -> builder.scalar(ExtendedScalars.DateTime);
    }
}
