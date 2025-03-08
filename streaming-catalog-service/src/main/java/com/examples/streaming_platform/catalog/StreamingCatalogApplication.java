package com.examples.streaming_platform.catalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class StreamingCatalogApplication {
    public static void main(String[] args) {
        SpringApplication.run(StreamingCatalogApplication.class, args);
    }
}