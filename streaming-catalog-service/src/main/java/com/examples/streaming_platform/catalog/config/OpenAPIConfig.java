package com.examples.streaming_platform.catalog.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configures OpenAPI documentation for the Streaming Catalog service.
 * Provides details such as API description, version, license, servers, and security schemes.
 */
@Configuration
@Slf4j
public class OpenAPIConfig {

    /**
     * Builds the OpenAPI documentation object.
     *
     * @return an OpenAPI instance describing the Streaming Catalog API
     */
    @Bean
    public OpenAPI streamingCatalogOpenAPI() {
        log.info("Initializing OpenAPI specification for Streaming Catalog.");

        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Streaming Catalog API")
                        .description("API documentation for the Streaming Platform Catalog Service")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Adil Faiyaz")
                                .email("adilmd98@gmail.com")
                                .url("https://github.com/adil-faiyaz98/streaming-platform.git"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server().url("http://localhost:8082").description("Local Development Server"),
                        new Server().url("https://api.streaming-platform.example.com").description("Test Server")
                ));
    }
}
