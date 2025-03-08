package com.examples.streaming_platform.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ContentSearchServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContentSearchServiceApplication.class, args);
    }
}