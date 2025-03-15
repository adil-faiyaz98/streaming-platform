package com.examples.streaming_platform.catalog.config;

import graphql.scalars.ExtendedScalars;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class GraphQLConfig {

    private static final Logger log = LoggerFactory.getLogger(GraphQLConfig.class);

    @Bean
    public RuntimeWiringConfigurer graphQLRuntimeWiringConfigurer() {  // Renamed method
        return builder -> builder.scalar(ExtendedScalars.DateTime);
    }
}