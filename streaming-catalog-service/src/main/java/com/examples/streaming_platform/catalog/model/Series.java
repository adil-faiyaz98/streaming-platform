// model/Series.java
package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;
import java.util.Set;

@Data
@Entity
@Table(name = "series")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    private Integer startYear;
    private Integer endYear;

    @ElementCollection
    @CollectionTable(name = "series_genres")
    private Set<String> genres;

    private String creator;
    private String maturityRating;
    private String imageUrl;

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL)
    private Set<Season> seasons;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "view_count")
    private Long viewCount;

    private Boolean featured;
    private Boolean ongoing;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (averageRating == null) averageRating = 0.0;
        if (viewCount == null) viewCount = 0L;
        if (featured == null) featured = false;
        if (ongoing == null) ongoing = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}