package com.examples.streaming_platform.catalog.config;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

import java.io.IOException;

@Configuration
public class GraphQLConfig {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.Date);
    }

    @Bean
    public GraphQlSource graphQlSource() throws IOException {
        return GraphQlSource.builder()
            .schemaResource(new ClassPathResource("graphql/schema.graphqls"))
            .build();
    }
}