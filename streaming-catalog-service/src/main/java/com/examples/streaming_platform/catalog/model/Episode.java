// model/Episode.java
package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Entity
@Table(name = "episodes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Episode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "season_id", updatable = false)
    private Long seasonId;

    @Column(name = "episode_number", nullable = false)
    private Integer episodeNumber;


    @Column(nullable = false)
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
    @JoinColumn(name = "season_id", nullable = false)
    @ToString.Exclude
    private Season season;

    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}