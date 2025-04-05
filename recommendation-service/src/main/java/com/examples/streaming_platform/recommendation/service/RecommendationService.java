package com.examples.streaming_platform.recommendation.service;

import com.examples.streaming_platform.recommendation.dto.RecommendationDTO;
import com.examples.streaming_platform.recommendation.model.ContentItem;
import com.examples.streaming_platform.recommendation.model.UserInteraction;
import com.examples.streaming_platform.recommendation.repository.UserInteractionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main service for generating recommendations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final CollaborativeFilteringService collaborativeFilteringService;
    private final ContentBasedFilteringService contentBasedFilteringService;
    private final ContentItemService contentItemService;
    private final UserInteractionRepository userInteractionRepository;
    
    @Value("${recommendation.features.collaborative-filtering:true}")
    private boolean enableCollaborativeFiltering;
    
    @Value("${recommendation.features.content-based:true}")
    private boolean enableContentBased;
    
    @Value("${recommendation.features.hybrid:true}")
    private boolean enableHybrid;
    
    @Value("${recommendation.features.trending:true}")
    private boolean enableTrending;

    /**
     * Get personalized recommendations for a user.
     *
     * @param userId the user ID
     * @param limit the maximum number of recommendations
     * @return a list of recommendations
     */
    public List<RecommendationDTO> getRecommendationsForUser(String userId, int limit) {
        log.debug("Generating recommendations for user: {}, limit: {}", userId, limit);
        
        List<RecommendationDTO> recommendations = new ArrayList<>();
        
        try {
            // Use hybrid approach if enabled
            if (enableHybrid) {
                recommendations = getHybridRecommendations(userId, limit);
            }
            // Otherwise, use individual algorithms
            else {
                if (enableCollaborativeFiltering) {
                    recommendations.addAll(collaborativeFilteringService.getUserBasedRecommendations(userId, limit / 2));
                }
                
                if (enableContentBased) {
                    recommendations.addAll(contentBasedFilteringService.getRecommendationsForUser(userId, limit / 2));
                }
            }
            
            // If we don't have enough recommendations, add trending items
            if (recommendations.size() < limit && enableTrending) {
                int remainingCount = limit - recommendations.size();
                recommendations.addAll(getTrendingRecommendations(remainingCount));
            }
            
            // Deduplicate and limit
            return deduplicateAndLimit(recommendations, limit);
            
        } catch (Exception e) {
            log.error("Error generating recommendations for user: {}", userId, e);
            
            // Fallback to trending recommendations
            return getTrendingRecommendations(limit);
        }
    }

    /**
     * Get similar content recommendations.
     *
     * @param itemId the reference item ID
     * @param limit the maximum number of recommendations
     * @return a list of recommendations
     */
    public List<RecommendationDTO> getSimilarContent(String itemId, int limit) {
        log.debug("Finding similar content for item: {}, limit: {}", itemId, limit);
        
        List<RecommendationDTO> recommendations = new ArrayList<>();
        
        try {
            // Get content-based recommendations
            if (enableContentBased) {
                recommendations.addAll(contentBasedFilteringService.getSimilarContent(itemId, limit));
            }
            
            // Add item-based collaborative filtering recommendations
            if (enableCollaborativeFiltering) {
                recommendations.addAll(collaborativeFilteringService.getItemBasedRecommendations(itemId, limit));
            }
            
            // Deduplicate and limit
            return deduplicateAndLimit(recommendations, limit);
            
        } catch (Exception e) {
            log.error("Error finding similar content for item: {}", itemId, e);
            
            // Fallback to popular items
            return getPopularRecommendations(limit);
        }
    }

    /**
     * Get trending content recommendations.
     *
     * @param limit the maximum number of recommendations
     * @return a list of recommendations
     */
    public List<RecommendationDTO> getTrendingRecommendations(int limit) {
        log.debug("Getting trending content, limit: {}", limit);
        
        try {
            // Get trending items from the last 7 days
            OffsetDateTime since = OffsetDateTime.now().minusDays(7);
            
            List<String> interactionTypes = Arrays.asList(
                    UserInteraction.InteractionType.VIEW.name(),
                    UserInteraction.InteractionType.RATING.name());
            
            List<String> trendingItemIds = userInteractionRepository.findTrendingItems(
                    interactionTypes, since, limit);
            
            // Convert to DTOs
            List<RecommendationDTO> recommendations = new ArrayList<>();
            
            for (int i = 0; i < trendingItemIds.size(); i++) {
                String itemId = trendingItemIds.get(i);
                
                Optional<ContentItem> contentItem = contentItemService.getContentItem(itemId);
                
                if (contentItem.isPresent()) {
                    recommendations.add(RecommendationDTO.builder()
                            .itemId(itemId)
                            .itemType(contentItem.get().getItemType().name())
                            .score(1.0 - (double) i / trendingItemIds.size()) // Simple score based on position
                            .algorithm("trending")
                            .reason("Trending this week")
                            .build());
                }
            }
            
            return recommendations;
            
        } catch (Exception e) {
            log.error("Error getting trending recommendations", e);
            
            // Fallback to popular items
            return getPopularRecommendations(limit);
        }
    }

    /**
     * Get popular content recommendations.
     *
     * @param limit the maximum number of recommendations
     * @return a list of recommendations
     */
    public List<RecommendationDTO> getPopularRecommendations(int limit) {
        log.debug("Getting popular content, limit: {}", limit);
        
        try {
            // Get most popular items
            List<ContentItem> popularItems = contentItemService.findMostPopular(limit);
            
            // Convert to DTOs
            List<RecommendationDTO> recommendations = new ArrayList<>();
            
            for (int i = 0; i < popularItems.size(); i++) {
                ContentItem item = popularItems.get(i);
                
                recommendations.add(RecommendationDTO.builder()
                        .itemId(item.getId())
                        .itemType(item.getItemType().name())
                        .score(1.0 - (double) i / popularItems.size()) // Simple score based on position
                        .algorithm("popularity")
                        .reason("Popular on our platform")
                        .build());
            }
            
            return recommendations;
            
        } catch (Exception e) {
            log.error("Error getting popular recommendations", e);
            return Collections.emptyList();
        }
    }

    /**
     * Get hybrid recommendations using multiple algorithms.
     *
     * @param userId the user ID
     * @param limit the maximum number of recommendations
     * @return a list of recommendations
     */
    private List<RecommendationDTO> getHybridRecommendations(String userId, int limit) {
        // Get recommendations from both algorithms
        List<RecommendationDTO> collaborativeRecs = enableCollaborativeFiltering ?
                collaborativeFilteringService.getUserBasedRecommendations(userId, limit) :
                Collections.emptyList();
        
        List<RecommendationDTO> contentBasedRecs = enableContentBased ?
                contentBasedFilteringService.getRecommendationsForUser(userId, limit) :
                Collections.emptyList();
        
        // Combine and re-rank
        Map<String, RecommendationDTO> combinedRecs = new HashMap<>();
        
        // Process collaborative filtering recommendations
        for (RecommendationDTO rec : collaborativeRecs) {
            rec.setScore(rec.getScore() * 0.6); // Weight collaborative filtering
            combinedRecs.put(rec.getItemId(), rec);
        }
        
        // Process content-based recommendations
        for (RecommendationDTO rec : contentBasedRecs) {
            if (combinedRecs.containsKey(rec.getItemId())) {
                // Item exists in both - combine scores
                RecommendationDTO existing = combinedRecs.get(rec.getItemId());
                existing.setScore(existing.getScore() + rec.getScore() * 0.4);
                existing.setAlgorithm("hybrid");
                existing.setReason("Recommended based on your preferences");
            } else {
                rec.setScore(rec.getScore() * 0.4); // Weight content-based
                combinedRecs.put(rec.getItemId(), rec);
            }
        }
        
        // Convert to list, sort by score, and limit
        return combinedRecs.values().stream()
                .sorted(Comparator.comparing(RecommendationDTO::getScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Deduplicate recommendations and limit to the specified count.
     *
     * @param recommendations the recommendations
     * @param limit the maximum number of recommendations
     * @return the deduplicated and limited recommendations
     */
    private List<RecommendationDTO> deduplicateAndLimit(List<RecommendationDTO> recommendations, int limit) {
        // Use a map to deduplicate by item ID
        Map<String, RecommendationDTO> deduplicated = new LinkedHashMap<>();
        
        for (RecommendationDTO rec : recommendations) {
            if (!deduplicated.containsKey(rec.getItemId())) {
                deduplicated.put(rec.getItemId(), rec);
            }
        }
        
        // Convert to list, sort by score, and limit
        return deduplicated.values().stream()
                .sorted(Comparator.comparing(RecommendationDTO::getScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
