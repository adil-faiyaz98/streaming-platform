package com.examples.streaming_platform.catalog.config;

import graphql.scalars.ExtendedScalars;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.GraphQlSource;

@Configuration
public class GraphQLConfig {
    
    @Bean
    public GraphQlSource graphQlSource() {
        return GraphQlSource.schemaResourceBuilder()
                .schemaResources(new ClassPathResource("graphql/schema.graphqls"))  
                .configureRuntimeWiring(builder -> 
                    builder.scalar(ExtendedScalars.DateTime)  
                )
                .build();
    }
}