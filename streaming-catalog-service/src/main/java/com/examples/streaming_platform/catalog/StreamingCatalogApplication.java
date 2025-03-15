package com.examples.streaming_platform.catalog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class StreamingCatalogApplication implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(StreamingCatalogApplication.class);
    private final Environment env;

    public StreamingCatalogApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(StreamingCatalogApplication.class, args);
    }

    @Override
    public void run(String... args) {
        logger.info("---------------------------------------------------------------------------");
        logger.info("Application '{}' is running with Spring profile(s) : {}", CouchbaseProperties.class.getPackage().getImplementationTitle(), env.getActiveProfiles());
        logger.info("---------------------------------------------------------------------------");
        logger.info("Streaming Catalog Service is up and running.");
        logger.info("---------------------------------------------------------------------------");
        logger.info("Accessible services and endpoints:");
        logger.info("---------------------------------------------------------------------------");
        logger.info("GraphQL: http://localhost:{}{}", env.getProperty("server.port"), env.getProperty("graphql.servlet.mapping", "/graphql"));
        logger.info("GraphiQL: http://localhost:{}{}", env.getProperty("server.port"), env.getProperty("graphql.graphiql.mapping", "/graphiql"));
        logger.info("---------------------------------------------------------------------------");
        logger.info("Swagger UI: http://localhost:{}{}", env.getProperty("server.port"), env.getProperty("springdoc.swagger-ui.path", "/swagger-ui"));
        logger.info("API Docs (JSON): http://localhost:{}{}", env.getProperty("server.port"), env.getProperty("springdoc.api-docs.path", "/v3/api-docs"));
        logger.info("API Docs (YAML):     http://localhost:{}{}", env.getProperty("server.port"), "/v3/api-docs.yaml");
        logger.info("---------------------------------------------------------------------------");
    }
}