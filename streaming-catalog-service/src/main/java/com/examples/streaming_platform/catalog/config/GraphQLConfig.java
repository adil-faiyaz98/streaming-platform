package com.examples.streaming_platform.catalog.config;

import graphql.scalars.ExtendedScalars;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.graphql.execution.GraphQlSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

/**
 * Configures the GraphQL schema and runtime wiring for this service.
 */
@Configuration
@Slf4j
@EnableWebMvc
public class GraphQLConfig {

    /**
     * Builds the GraphQL schema from the `classpath:schema/graphql.graphqls` resource
     * and configures the DateTime scalar.
     *
     * @return a GraphQlSource configured with custom scalar support
     */
    @Bean
    public GraphQlSource graphQlSource() {
        log.info("Initializing GraphQlSource with custom scalars and schema resources.");

        return GraphQlSource.schemaResourceBuilder()
                .schemaResources(new ClassPathResource("schema/graphql.graphqls"))
                .configureRuntimeWiring(builder ->
                        builder.scalar(ExtendedScalars.DateTime)
                )
                .build();
    }
}