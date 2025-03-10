package com.examples.streaming_platform.catalog.config;

import graphql.scalars.ExtendedScalars;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.graphql.execution.GraphQlSource;

/**
 * Configures the GraphQL schema and runtime wiring for this service.
 */
@Configuration
@Slf4j
public class GraphQLConfig {

    /**
     * Builds the GraphQL schema from the `classpath:graphql/schema.graphqls` resource
     * and configures the DateTime scalar.
     *
     * @return a GraphQlSource configured with custom scalar support
     */
    @Bean
    public GraphQlSource graphQlSource() {
        log.info("Initializing GraphQlSource with custom scalars and schema resources.");

        return GraphQlSource.schemaResourceBuilder()
                .schemaResources(new ClassPathResource("graphql/schema.graphqls"))
                .configureRuntimeWiring(builder ->
                        builder.scalar(ExtendedScalars.DateTime)
                )
                .build();
    }
}
