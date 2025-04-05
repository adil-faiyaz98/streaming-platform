package com.examples.streaming_platform.recommendation.dto;

import com.examples.streaming_platform.recommendation.model.UserInteraction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.Map;

/**
 * DTO for user interaction events.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInteractionDTO {

    /**
     * The user ID.
     */
    @NotBlank(message = "User ID is required")
    private String userId;
    
    /**
     * The item ID.
     */
    @NotBlank(message = "Item ID is required")
    private String itemId;
    
    /**
     * The item type.
     */
    @NotNull(message = "Item type is required")
    private UserInteraction.ItemType itemType;
    
    /**
     * The interaction type.
     */
    @NotNull(message = "Interaction type is required")
    private UserInteraction.InteractionType interactionType;
    
    /**
     * The interaction value (e.g., rating value, watch duration).
     */
    private Double value;
    
    /**
     * The timestamp of the interaction.
     */
    private OffsetDateTime timestamp;
    
    /**
     * Additional context data for the interaction.
     */
    private Map<String, Object> contextData;
}
