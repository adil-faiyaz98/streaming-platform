package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Represents an Episode entity within a Season.
 */
@Entity
@Table(name = "episodes")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "episode_number", nullable = false)
    @ToString.Include
    private Integer episodeNumber;

    @Column(nullable = false)
    @ToString.Include
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 1000)
    private String overview;

    private Integer duration; // in minutes

    @Column(name = "air_date")
    private LocalDate airDate;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "season_id")
    private Season season;

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

    public Episode(Long id, Integer episodeNumber, String title, String description, String overview,
                   Integer duration, LocalDate airDate, String imageUrl, String videoUrl, Season season,
                   OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.id = id;
        this.episodeNumber = episodeNumber;
        this.title = title;
        this.description = description;
        this.overview = overview;
        this.duration = duration;
        this.airDate = airDate;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.season = season;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
