package com.examples.streaming_platform.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main application class for the Recommendation Service.
 * This service provides AI-powered content recommendations for the streaming platform.
 */
@SpringBootApplication
@EnableScheduling
public class RecommendationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecommendationServiceApplication.class, args);
    }
}
