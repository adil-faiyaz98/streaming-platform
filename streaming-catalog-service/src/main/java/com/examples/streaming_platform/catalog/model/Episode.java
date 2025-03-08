// model/Episode.java
package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Instant;

@Data
@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    private Integer episodeNumber;
    private String title;

    @Column(length = 2000)
    private String description;

    private Integer duration; // in minutes
    private String videoUrl;
    private String thumbnailUrl;

    @Column(name = "view_count")
    private Long viewCount;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
        if (viewCount == null) viewCount = 0L;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}