package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents a Movie entity.
 * <p>
 * Must have at least one genre from the official list:
 * Action, Adventure, Comedy, Drama, Horror, Thriller, Romance, Science Fiction,
 * Fantasy, Mystery, Documentary, Animation, Crime, Musical, Biographical, Historical.
 */
@Entity
@Table(
        name = "movies",
        indexes = {
                @Index(name = "idx_movies_title", columnList = "title"),
                @Index(name = "idx_movies_featured", columnList = "featured"),
                @Index(name = "idx_movies_release_year", columnList = "releaseYear")
        }
)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    @ToString.Include
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

    // ----- New Fields -----

    /** The primary spoken language of the movie (e.g. "English", "Spanish"). */
    @Column(name = "language")
    private String language;

    /** The country where the movie was produced (e.g. "USA", "UK"). */
    @Column(name = "country_of_origin")
    private String countryOfOrigin;

    /** A URL for subtitles (if applicable). */
    @Column(name = "subtitle_url")
    private String subtitleUrl;

    /** Awards the movie has won or been nominated for. */
    @Column(name = "awards", length = 2000)
    private String awards;

    /** The production budget of the movie. */
    @Column(name = "budget", precision = 15, scale = 2)
    private BigDecimal budget;

    /** The worldwide box office gross. */
    @Column(name = "box_office", precision = 15, scale = 2)
    private BigDecimal boxOffice;

    /**
     * A collection of cast members (could be actor names, or references).
     * For advanced modeling, consider separate Actor or Person entities.
     */
    @ElementCollection
    @CollectionTable(name = "movie_cast", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "cast_member")
    private Set<String> cast = new HashSet<>();

    /**
     * A collection of crew members (director of photography, producers, etc.).
     * For advanced modeling, consider separate Crew/Person entities.
     */
    @ElementCollection
    @CollectionTable(name = "movie_crew", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "crew_member")
    private Set<String> crew = new HashSet<>();

    // ----- Existing Fields -----

    @Column(name = "created_at", updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @ElementCollection(targetClass = Genre.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genres")
    private Set<String> genres = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = OffsetDateTime.now();
        this.updatedAt = this.createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = OffsetDateTime.now();
    }

    public Movie(Long id, String title, String description, Integer releaseYear, LocalDate releaseDate,
                 String director, Integer duration, String maturityRating, String imageUrl, String videoUrl,
                 String posterUrl, Float averageRating, Integer viewCount, Boolean featured, double rating,
                 String language, String countryOfOrigin, String subtitleUrl, String awards,
                 BigDecimal budget, BigDecimal boxOffice, Set<String> cast, Set<String> crew,
                 OffsetDateTime createdAt, OffsetDateTime updatedAt, Set<String> genres) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseYear = releaseYear;
        this.releaseDate = releaseDate;
        this.director = director;
        this.duration = duration;
        this.maturityRating = maturityRating;
        this.imageUrl = imageUrl;
        this.videoUrl = videoUrl;
        this.posterUrl = posterUrl;
        this.averageRating = averageRating;
        this.viewCount = viewCount != null ? viewCount : 0;
        this.featured = featured != null ? featured : false;
        this.rating = rating;
        this.language = language;
        this.countryOfOrigin = countryOfOrigin;
        this.subtitleUrl = subtitleUrl;
        this.awards = awards;
        this.budget = budget;
        this.boxOffice = boxOffice;
        if (cast != null) {
            this.cast = cast;
        }
        if (crew != null) {
            this.crew = crew;
        }
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        if (genres != null) {
            this.genres = genres;
        }
    }
}
