package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.ExternalMovieInfoDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

/**
 * Service for interacting with external APIs with resilience patterns.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalApiService {

    private final RestTemplate restTemplate;

    @Value("${app.external-api.movie-info-url:https://api.example.com/movies}")
    private String movieInfoApiUrl;

    /**
     * Get movie information from an external API with circuit breaker, retry, and time limiter.
     *
     * @param movieId the movie ID
     * @return a CompletableFuture containing the movie information
     */
    @CircuitBreaker(name = "externalMovieInfo", fallbackMethod = "getMovieInfoFallback")
    @Retry(name = "externalMovieInfo")
    @TimeLimiter(name = "externalMovieInfo")
    public CompletableFuture<ExternalMovieInfoDTO> getMovieInfo(String movieId) {
        log.debug("Fetching movie info from external API for movie ID: {}", movieId);
        return CompletableFuture.supplyAsync(() -> {
            String url = movieInfoApiUrl + "/" + movieId;
            return restTemplate.getForObject(url, ExternalMovieInfoDTO.class);
        });
    }

    /**
     * Fallback method for getMovieInfo when the circuit is open or an exception occurs.
     *
     * @param movieId the movie ID
     * @param ex the exception that triggered the fallback
     * @return a CompletableFuture containing a default movie information
     */
    private CompletableFuture<ExternalMovieInfoDTO> getMovieInfoFallback(String movieId, Exception ex) {
        log.warn("Fallback for movie info. Movie ID: {}, Exception: {}", movieId, ex.getMessage());
        
        if (ex instanceof TimeoutException) {
            log.error("Timeout while fetching movie info from external API", ex);
        } else {
            log.error("Error fetching movie info from external API", ex);
        }
        
        // Return a default/cached response
        ExternalMovieInfoDTO fallbackInfo = new ExternalMovieInfoDTO();
        fallbackInfo.setId(movieId);
        fallbackInfo.setTitle("Unavailable");
        fallbackInfo.setDescription("Movie information temporarily unavailable");
        
        return CompletableFuture.completedFuture(fallbackInfo);
    }
}
