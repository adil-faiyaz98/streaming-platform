package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies", indexes = {
        @Index(name = "idx_movies_title", columnList = "title"),
        @Index(name = "idx_movies_featured", columnList = "featured"),
        @Index(name = "idx_movies_release_year", columnList = "releaseYear")
})

public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "release_year")
    private Integer releaseYear;

    @Column(name = "release_date")
    private LocalDate releaseDate;

    private String director;

    private Integer duration; // in minutes

    @Column(name = "maturity_rating")
    private String maturityRating;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "poster_url")
    private String posterUrl;

    @Column(name = "average_rating")
    private Float averageRating;

    @Column(name = "view_count")
    private Integer viewCount = 0;

    private Boolean featured = false;

    @Column(name = "rating")
    private double rating;

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ElementCollection
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genres")
    private Set<String> genres = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = OffsetDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}