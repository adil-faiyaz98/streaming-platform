package com.examples.streaming_platform.recommendation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

/**
 * Entity representing a user's interaction with content.
 * This is the primary data source for collaborative filtering recommendations.
 */
@Entity
@Table(name = "user_interactions", 
       indexes = {
           @Index(name = "idx_user_item", columnList = "user_id, item_id"),
           @Index(name = "idx_item_type", columnList = "item_id, interaction_type"),
           @Index(name = "idx_timestamp", columnList = "timestamp")
       })
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInteraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "item_id", nullable = false)
    private String itemId;

    @Column(name = "item_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ItemType itemType;

    @Column(name = "interaction_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private InteractionType interactionType;

    @Column(name = "value")
    private Double value;

    @Column(name = "timestamp", nullable = false)
    private OffsetDateTime timestamp;

    @Column(name = "context_data", columnDefinition = "jsonb")
    private String contextData;

    /**
     * Type of content item.
     */
    public enum ItemType {
        MOVIE,
        SERIES,
        EPISODE
    }

    /**
     * Type of user interaction.
     */
    public enum InteractionType {
        VIEW,           // User viewed the content
        RATING,         // User rated the content
        WATCH_TIME,     // User watched for a specific duration
        LIKE,           // User liked the content
        DISLIKE,        // User disliked the content
        ADD_TO_LIST,    // User added to watchlist
        SEARCH,         // User searched for the content
        CLICK           // User clicked on the content
    }
}
