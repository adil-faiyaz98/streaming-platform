package com.examples.streaming_platform.recommendation.service;

import com.examples.streaming_platform.recommendation.dto.RecommendationDTO;
import com.examples.streaming_platform.recommendation.model.UserInteraction;
import com.examples.streaming_platform.recommendation.repository.UserInteractionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.PostgreSQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for collaborative filtering recommendations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CollaborativeFilteringService {

    private final UserInteractionRepository userInteractionRepository;
    private final DataSource dataSource;
    
    @Value("${recommendation.algorithm.neighborhood-size:50}")
    private int neighborhoodSize;
    
    @Value("${recommendation.algorithm.similarity-threshold:0.1}")
    private double similarityThreshold;

    /**
     * Get user-based collaborative filtering recommendations.
     *
     * @param userId the user ID
     * @param limit the maximum number of recommendations
     * @return a list of recommendations
     */
    public List<RecommendationDTO> getUserBasedRecommendations(String userId, int limit) {
        try {
            // Create data model from database
            DataModel dataModel = new PostgreSQLJDBCDataModel(
                    dataSource,
                    "user_interactions",
                    "user_id",
                    "item_id",
                    "value",
                    "timestamp");
            
            // Define user similarity metric
            UserSimilarity similarity = new PearsonCorrelationSimilarity(dataModel);
            
            // Define user neighborhood
            UserNeighborhood neighborhood = new ThresholdUserNeighborhood(
                    similarityThreshold, similarity, dataModel);
            
            // Create recommender
            UserBasedRecommender recommender = new GenericUserBasedRecommender(
                    dataModel, neighborhood, similarity);
            
            // Get recommendations
            List<RecommendedItem> recommendedItems = recommender.recommend(
                    Long.parseLong(userId), limit);
            
            // Convert to DTOs
            return recommendedItems.stream()
                    .map(item -> RecommendationDTO.builder()
                            .itemId(String.valueOf(item.getItemID()))
                            .score(item.getValue())
                            .algorithm("collaborative_filtering")
                            .reason("Based on similar users' preferences")
                            .build())
                    .collect(Collectors.toList());
            
        } catch (TasteException e) {
            log.error("Error generating collaborative filtering recommendations", e);
            return new ArrayList<>();
        }
    }

    /**
     * Get item-based recommendations using co-occurrence.
     *
     * @param itemId the item ID
     * @param limit the maximum number of recommendations
     * @return a list of recommendations
     */
    public List<RecommendationDTO> getItemBasedRecommendations(String itemId, int limit) {
        // Get co-occurring items
        List<String> cooccurringItems = userInteractionRepository.findCooccurringItems(
                itemId,
                UserInteraction.InteractionType.VIEW.name(),
                3);
        
        // Convert to DTOs
        List<RecommendationDTO> recommendations = new ArrayList<>();
        
        for (int i = 0; i < Math.min(cooccurringItems.size(), limit); i++) {
            String cooccurringItemId = cooccurringItems.get(i);
            
            recommendations.add(RecommendationDTO.builder()
                    .itemId(cooccurringItemId)
                    .score(1.0 - (double) i / cooccurringItems.size()) // Simple score based on position
                    .algorithm("item_cooccurrence")
                    .reason("Users who viewed this also viewed")
                    .build());
        }
        
        return recommendations;
    }
}
