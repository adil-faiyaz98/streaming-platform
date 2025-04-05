package com.examples.streaming_platform.recommendation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for recommendation responses.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationDTO {

    /**
     * The ID of the recommended item.
     */
    private String itemId;
    
    /**
     * The type of the recommended item (movie, series, episode).
     */
    private String itemType;
    
    /**
     * The recommendation score (higher is better).
     */
    private Double score;
    
    /**
     * The reason for the recommendation.
     */
    private String reason;
    
    /**
     * The algorithm that generated this recommendation.
     */
    private String algorithm;
}
