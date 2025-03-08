// model/Season.java
package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "seasons")
@NoArgsConstructor
@AllArgsConstructor
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer seasonNumber;
    private String title;

    @Column(length = 1000)
    private String overview;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "air_date")
    private LocalDate airDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tv_show_id", nullable = false)
    @ToString.Exclude
    private TvShow tvShow;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", nullable = false)
    private Series series;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @OneToMany(mappedBy = "seasonId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Episode> episodes = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }

    public Season(Integer seasonNumber, String title, String overview, String posterUrl, LocalDate airDate) {
        this.seasonNumber = seasonNumber;
        this.title = title;
        this.overview = overview;
        this.posterUrl = posterUrl;
        this.airDate = airDate;
    }
}