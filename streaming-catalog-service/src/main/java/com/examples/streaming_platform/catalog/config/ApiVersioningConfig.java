package com.examples.streaming_platform.catalog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for API versioning and documentation.
 */
@Configuration
public class ApiVersioningConfig {

    /**
     * Configure OpenAPI documentation with version information.
     *
     * @return the OpenAPI configuration
     */
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("Streaming Platform Catalog API")
                        .description("API for managing streaming platform catalog content")
                        .version("2.0.0")
                        .contact(new Contact()
                                .name("Streaming Platform Team")
                                .email("api@streaming-platform.example.com")
                                .url("https://streaming-platform.example.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")));
    }
}
