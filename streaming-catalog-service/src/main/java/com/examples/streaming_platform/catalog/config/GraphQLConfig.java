package com.examples.streaming_platform.catalog.config;

import graphql.GraphQL;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.graphql.execution.GraphQlSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@Configuration
public class GraphQLConfig {
    
    @Bean
    public GraphQlSource graphQlSource() throws IOException {
        // Load schema from resource
        Resource schemaResource = new ClassPathResource("graphql/schema.graphqls");
        String sdl = new String(Files.readAllBytes(schemaResource.getFile().toPath()), StandardCharsets.UTF_8);
        
        // Parse schema
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring().build();
        
        // Create schema
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeRegistry, runtimeWiring);
        
        // Return GraphQlSource with schema
        return GraphQlSource.schemaResourceBuilder()
                .schemaResources(new ClassPathResource("graphql/schema.graphqls"))
                .configureRuntimeWiring(builder -> builder.scalar(ExtendedScalars.DateTime)
                )
                .build();
    }
}