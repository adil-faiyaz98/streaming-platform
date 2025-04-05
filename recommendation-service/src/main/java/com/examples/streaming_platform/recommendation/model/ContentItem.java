package com.examples.streaming_platform.recommendation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Entity representing content items (movies, series, episodes) with their features.
 * This is used for content-based filtering recommendations.
 */
@Entity
@Table(name = "content_items")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContentItem {

    @Id
    @Column(name = "item_id")
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "item_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserInteraction.ItemType itemType;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "director")
    private String director;

    @Column(name = "duration")
    private Integer duration;

    @Column(name = "maturity_rating")
    private String maturityRating;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "popularity_score")
    private Double popularityScore;

    @Column(name = "last_updated", nullable = false)
    private OffsetDateTime lastUpdated;

    @ElementCollection
    @CollectionTable(
        name = "content_item_genres",
        joinColumns = @JoinColumn(name = "item_id")
    )
    @Column(name = "genre")
    private Set<String> genres = new HashSet<>();

    @ElementCollection
    @CollectionTable(
        name = "content_item_actors",
        joinColumns = @JoinColumn(name = "item_id")
    )
    @Column(name = "actor")
    private Set<String> actors = new HashSet<>();

    @ElementCollection
    @CollectionTable(
        name = "content_item_keywords",
        joinColumns = @JoinColumn(name = "item_id")
    )
    @Column(name = "keyword")
    private Set<String> keywords = new HashSet<>();

    @Column(name = "feature_vector", columnDefinition = "bytea")
    private byte[] featureVector;
}
