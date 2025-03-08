package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.Instant;
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
    @NotBlank
    private String title;

    @Column(length = 2000)
    private String description;

    @Min(1900)
    private Integer releaseYear;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    private Set<String> genres;

    private String director;

    @Min(1)
    private Integer duration;

    private String maturityRating;

    private String imageUrl;

    private String videoUrl;

    @Column(name = "average_rating")
    @Min(1)
    @Max(10)
    private Double averageRating = 0.0;

    @Column(name = "view_count")
    private Long viewCount;

    private Boolean featured=false;

    @Column(name = "created_at", updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = Instant.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }
}