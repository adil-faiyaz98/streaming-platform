package com.examples.streaming_platform.recommendation.service;

import com.examples.streaming_platform.recommendation.dto.RecommendationDTO;
import com.examples.streaming_platform.recommendation.model.ContentItem;
import com.examples.streaming_platform.recommendation.model.UserInteraction;
import com.examples.streaming_platform.recommendation.repository.ContentItemRepository;
import com.examples.streaming_platform.recommendation.repository.UserInteractionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service for content-based filtering recommendations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContentBasedFilteringService {

    private final ContentItemRepository contentItemRepository;
    private final UserInteractionRepository userInteractionRepository;

    /**
     * Get content-based recommendations for a user.
     *
     * @param userId the user ID
     * @param limit the maximum number of recommendations
     * @return a list of recommendations
     */
    public List<RecommendationDTO> getRecommendationsForUser(String userId, int limit) {
        // Get user's recent positive interactions
        List<UserInteraction> userInteractions = userInteractionRepository
                .findByUserIdAndInteractionType(
                        userId,
                        UserInteraction.InteractionType.RATING,
                        PageRequest.of(0, 20, Sort.by("timestamp").descending()))
                .getContent();
        
        // Extract genres from items the user has interacted with positively
        Set<String> userPreferredGenres = new HashSet<>();
        Map<String, Integer> genreFrequency = new HashMap<>();
        
        for (UserInteraction interaction : userInteractions) {
            // Only consider positive ratings (> 3.5)
            if (interaction.getValue() != null && interaction.getValue() > 3.5) {
                Optional<ContentItem> contentItem = contentItemRepository.findById(interaction.getItemId());
                
                if (contentItem.isPresent()) {
                    for (String genre : contentItem.get().getGenres()) {
                        userPreferredGenres.add(genre);
                        genreFrequency.put(genre, genreFrequency.getOrDefault(genre, 0) + 1);
                    }
                }
            }
        }
        
        // If user has no preferred genres, return empty list
        if (userPreferredGenres.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Find content with matching genres
        List<ContentItem> matchingContent = contentItemRepository.findByGenresIn(userPreferredGenres);
        
        // Filter out items the user has already interacted with
        Set<String> interactedItemIds = userInteractions.stream()
                .map(UserInteraction::getItemId)
                .collect(Collectors.toSet());
        
        List<ContentItem> newMatchingContent = matchingContent.stream()
                .filter(item -> !interactedItemIds.contains(item.getId()))
                .collect(Collectors.toList());
        
        // Score items based on genre overlap with user preferences
        List<ScoredItem> scoredItems = new ArrayList<>();
        
        for (ContentItem item : newMatchingContent) {
            double score = calculateGenreMatchScore(item.getGenres(), genreFrequency);
            scoredItems.add(new ScoredItem(item, score));
        }
        
        // Sort by score and limit
        scoredItems.sort(Comparator.comparing(ScoredItem::getScore).reversed());
        
        // Convert to DTOs
        List<RecommendationDTO> recommendations = new ArrayList<>();
        
        for (int i = 0; i < Math.min(scoredItems.size(), limit); i++) {
            ScoredItem scoredItem = scoredItems.get(i);
            ContentItem item = scoredItem.getItem();
            
            recommendations.add(RecommendationDTO.builder()
                    .itemId(item.getId())
                    .itemType(item.getItemType().name())
                    .score(scoredItem.getScore())
                    .algorithm("content_based")
                    .reason("Based on your genre preferences")
                    .build());
        }
        
        return recommendations;
    }

    /**
     * Get similar content based on genre overlap.
     *
     * @param itemId the reference item ID
     * @param limit the maximum number of recommendations
     * @return a list of recommendations
     */
    public List<RecommendationDTO> getSimilarContent(String itemId, int limit) {
        // Get similar content from repository
        List<ContentItem> similarContent = contentItemRepository.findSimilarContentByGenres(itemId, 1, limit);
        
        // Convert to DTOs
        List<RecommendationDTO> recommendations = new ArrayList<>();
        
        for (ContentItem item : similarContent) {
            recommendations.add(RecommendationDTO.builder()
                    .itemId(item.getId())
                    .itemType(item.getItemType().name())
                    .score(0.9) // Default score for similar content
                    .algorithm("content_based")
                    .reason("Similar to content you've watched")
                    .build());
        }
        
        return recommendations;
    }

    /**
     * Calculate a score based on genre match with user preferences.
     *
     * @param itemGenres the item's genres
     * @param userGenreFrequency the user's genre preferences with frequency
     * @return the match score
     */
    private double calculateGenreMatchScore(Set<String> itemGenres, Map<String, Integer> userGenreFrequency) {
        double score = 0.0;
        int totalUserGenreCount = userGenreFrequency.values().stream().mapToInt(Integer::intValue).sum();
        
        for (String genre : itemGenres) {
            if (userGenreFrequency.containsKey(genre)) {
                // Weight by frequency of genre in user preferences
                score += (double) userGenreFrequency.get(genre) / totalUserGenreCount;
            }
        }
        
        return score;
    }

    /**
     * Helper class for scoring items.
     */
    private static class ScoredItem {
        private final ContentItem item;
        private final double score;

        public ScoredItem(ContentItem item, double score) {
            this.item = item;
            this.score = score;
        }

        public ContentItem getItem() {
            return item;
        }

        public double getScore() {
            return score;
        }
    }
}
