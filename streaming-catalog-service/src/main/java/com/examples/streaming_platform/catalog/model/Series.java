package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Series entity within the streaming platform.
 * Typically can have multiple Seasons, each with multiple Episodes.
 */
@Entity
@Table(name = "series")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Series {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 255)
    @ToString.Include
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "start_year")
    private Integer startYear;

    @Column(name = "end_year")
    private Integer endYear;

    @Column(name = "maturity_rating")
    private String maturityRating;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "average_rating")
    private Double averageRating = 0.0;

    @Column(name = "view_count")
    private Long viewCount = 0L;

    @Column
    private Boolean featured = false;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "series_genres", joinColumns = @JoinColumn(name = "series_id"))
    @Column(name = "genre")
    private Set<String> genres = new HashSet<>();

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Season> seasons = new HashSet<>();

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public Series(Long id, String title, String description, Integer startYear, Integer endYear,
                  String maturityRating, String imageUrl, Double averageRating, Long viewCount,
                  Boolean featured, Set<String> genres, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startYear = startYear;
        this.endYear = endYear;
        this.maturityRating = maturityRating;
        this.imageUrl = imageUrl;
        this.averageRating = (averageRating != null) ? averageRating : 0.0;
        this.viewCount = (viewCount != null) ? viewCount : 0L;
        this.featured = (featured != null) ? featured : false;
        this.genres = (genres != null) ? genres : new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
