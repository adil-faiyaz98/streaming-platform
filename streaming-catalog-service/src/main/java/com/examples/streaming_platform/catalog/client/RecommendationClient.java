package com.examples.streaming_platform.catalog.client;

import com.examples.streaming_platform.catalog.dto.RecommendationDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Client for interacting with the Recommendation Service.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class RecommendationClient {

    private final RestTemplate restTemplate;
    
    @Value("${external-services.recommendation-service.url:http://localhost:8083/api/v1}")
    private String recommendationServiceUrl;
    
    /**
     * Get personalized recommendations for a user.
     *
     * @param userId the user ID
     * @param limit the maximum number of recommendations
     * @return a list of recommendation DTOs
     */
    @CircuitBreaker(name = "recommendationService", fallbackMethod = "getDefaultRecommendations")
    @Retry(name = "recommendationService")
    @TimeLimiter(name = "recommendationService")
    public CompletableFuture<List<RecommendationDTO>> getRecommendationsForUser(String userId, int limit) {
        return CompletableFuture.supplyAsync(() -> {
            String url = String.format("%s/recommendations/user/%s?limit=%d", 
                    recommendationServiceUrl, userId, limit);
            
            ResponseEntity<List<RecommendationDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<RecommendationDTO>>() {}
            );
            
            return response.getBody();
        });
    }
    
    /**
     * Get similar content recommendations.
     *
     * @param itemId the reference item ID
     * @param limit the maximum number of recommendations
     * @return a list of recommendation DTOs
     */
    @CircuitBreaker(name = "recommendationService", fallbackMethod = "getDefaultRecommendations")
    @Retry(name = "recommendationService")
    @TimeLimiter(name = "recommendationService")
    public CompletableFuture<List<RecommendationDTO>> getSimilarContent(String itemId, int limit) {
        return CompletableFuture.supplyAsync(() -> {
            String url = String.format("%s/recommendations/similar/%s?limit=%d", 
                    recommendationServiceUrl, itemId, limit);
            
            ResponseEntity<List<RecommendationDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<RecommendationDTO>>() {}
            );
            
            return response.getBody();
        });
    }
    
    /**
     * Get trending content recommendations.
     *
     * @param limit the maximum number of recommendations
     * @return a list of recommendation DTOs
     */
    @CircuitBreaker(name = "recommendationService", fallbackMethod = "getDefaultRecommendations")
    @Retry(name = "recommendationService")
    @TimeLimiter(name = "recommendationService")
    public CompletableFuture<List<RecommendationDTO>> getTrendingContent(int limit) {
        return CompletableFuture.supplyAsync(() -> {
            String url = String.format("%s/recommendations/trending?limit=%d", 
                    recommendationServiceUrl, limit);
            
            ResponseEntity<List<RecommendationDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<RecommendationDTO>>() {}
            );
            
            return response.getBody();
        });
    }
    
    /**
     * Fallback method for recommendation service calls.
     *
     * @param userId the user ID
     * @param limit the maximum number of recommendations
     * @param ex the exception that triggered the fallback
     * @return a default list of recommendations
     */
    private CompletableFuture<List<RecommendationDTO>> getDefaultRecommendations(String userId, int limit, Exception ex) {
        log.warn("Using fallback recommendations due to: {}", ex.getMessage());
        return CompletableFuture.completedFuture(new ArrayList<>());
    }
    
    /**
     * Fallback method for recommendation service calls.
     *
     * @param itemId the item ID
     * @param limit the maximum number of recommendations
     * @param ex the exception that triggered the fallback
     * @return a default list of recommendations
     */
    private CompletableFuture<List<RecommendationDTO>> getDefaultRecommendations(String itemId, int limit, Exception ex) {
        log.warn("Using fallback recommendations due to: {}", ex.getMessage());
        return CompletableFuture.completedFuture(new ArrayList<>());
    }
    
    /**
     * Fallback method for recommendation service calls.
     *
     * @param limit the maximum number of recommendations
     * @param ex the exception that triggered the fallback
     * @return a default list of recommendations
     */
    private CompletableFuture<List<RecommendationDTO>> getDefaultRecommendations(int limit, Exception ex) {
        log.warn("Using fallback recommendations due to: {}", ex.getMessage());
        return CompletableFuture.completedFuture(new ArrayList<>());
    }
}
