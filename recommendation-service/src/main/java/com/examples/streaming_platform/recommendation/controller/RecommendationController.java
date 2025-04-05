package com.examples.streaming_platform.recommendation.controller;

import com.examples.streaming_platform.recommendation.dto.RecommendationDTO;
import com.examples.streaming_platform.recommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for recommendation endpoints.
 */
@RestController
@RequestMapping("/api/v1/recommendations")
@RequiredArgsConstructor
@Tag(name = "Recommendations", description = "API for AI-powered content recommendations")
@Slf4j
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * Get personalized recommendations for a user.
     *
     * @param userId the user ID
     * @param limit the maximum number of recommendations
     * @return a list of recommendations
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get personalized recommendations for a user")
    public ResponseEntity<List<RecommendationDTO>> getRecommendationsForUser(
            @PathVariable String userId,
            @RequestParam(defaultValue = "10") 
            @Parameter(description = "Maximum number of recommendations to return") int limit) {
        
        log.debug("Getting recommendations for user: {}, limit: {}", userId, limit);
        return ResponseEntity.ok(recommendationService.getRecommendationsForUser(userId, limit));
    }

    /**
     * Get similar content recommendations.
     *
     * @param itemId the reference item ID
     * @param limit the maximum number of recommendations
     * @return a list of recommendations
     */
    @GetMapping("/similar/{itemId}")
    @Operation(summary = "Get similar content recommendations")
    public ResponseEntity<List<RecommendationDTO>> getSimilarContent(
            @PathVariable String itemId,
            @RequestParam(defaultValue = "10") 
            @Parameter(description = "Maximum number of recommendations to return") int limit) {
        
        log.debug("Getting similar content for item: {}, limit: {}", itemId, limit);
        return ResponseEntity.ok(recommendationService.getSimilarContent(itemId, limit));
    }

    /**
     * Get trending content recommendations.
     *
     * @param limit the maximum number of recommendations
     * @return a list of recommendations
     */
    @GetMapping("/trending")
    @Operation(summary = "Get trending content")
    public ResponseEntity<List<RecommendationDTO>> getTrendingContent(
            @RequestParam(defaultValue = "10") 
            @Parameter(description = "Maximum number of recommendations to return") int limit) {
        
        log.debug("Getting trending content, limit: {}", limit);
        return ResponseEntity.ok(recommendationService.getTrendingRecommendations(limit));
    }

    /**
     * Get popular content recommendations.
     *
     * @param limit the maximum number of recommendations
     * @return a list of recommendations
     */
    @GetMapping("/popular")
    @Operation(summary = "Get popular content")
    public ResponseEntity<List<RecommendationDTO>> getPopularContent(
            @RequestParam(defaultValue = "10") 
            @Parameter(description = "Maximum number of recommendations to return") int limit) {
        
        log.debug("Getting popular content, limit: {}", limit);
        return ResponseEntity.ok(recommendationService.getPopularRecommendations(limit));
    }
}
