package com.examples.streaming_platform.recommendation.dto;

import com.examples.streaming_platform.recommendation.model.UserInteraction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

/**
 * DTO for content item data.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentItemDTO {

    /**
     * The item ID.
     */
    @NotBlank(message = "Item ID is required")
    private String id;
    
    /**
     * The item title.
     */
    @NotBlank(message = "Title is required")
    private String title;
    
    /**
     * The item type.
     */
    @NotNull(message = "Item type is required")
    private UserInteraction.ItemType itemType;
    
    /**
     * The release year.
     */
    private Integer releaseYear;
    
    /**
     * The director.
     */
    private String director;
    
    /**
     * The duration in minutes.
     */
    private Integer duration;
    
    /**
     * The maturity rating.
     */
    private String maturityRating;
    
    /**
     * The average user rating.
     */
    private Double averageRating;
    
    /**
     * The genres.
     */
    private Set<String> genres = new HashSet<>();
    
    /**
     * The actors.
     */
    private Set<String> actors = new HashSet<>();
    
    /**
     * The keywords.
     */
    private Set<String> keywords = new HashSet<>();
}
