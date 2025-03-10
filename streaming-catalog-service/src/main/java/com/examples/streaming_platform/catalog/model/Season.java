package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Season entity within a TV Show or Series.
 */
@Entity
@Table(name = "seasons")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Season {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "season_number", nullable = false)
    @ToString.Include
    private Integer seasonNumber;

    @Column(nullable = false)
    @ToString.Include
    private String title;

    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id")
    private Series series;

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Episode> episodes = new ArrayList<>();

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_show_id")
    @ToString.Exclude
    private TvShow tvShow;

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

    public Season(Long id, Integer seasonNumber, String title, String description, String imageUrl,
                  Series series, List<Episode> episodes, OffsetDateTime createdAt, OffsetDateTime updatedAt,
                  TvShow tvShow) {
        this.id = id;
        this.seasonNumber = seasonNumber;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
        this.series = series;
        this.episodes = (episodes != null) ? episodes : new ArrayList<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.tvShow = tvShow;
    }
}
