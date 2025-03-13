package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a high-level TV Show entity, which can contain multiple Seasons.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "tv_shows")
public class TvShow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 255)
    @ToString.Include
    private String title;

    @Column(length = 2000)
    private String description;

    @Column(name = "first_air_date")
    private LocalDate firstAirDate;

    private Double rating = 0.0;

    @Column(nullable = false)
    private Boolean featured = false;

    @Column(name = "view_count", nullable = false)
    private Long viewCount = 0L;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "tv_genres", joinColumns = @JoinColumn(name = "tv_show_id"))
    @ToString.Include
    @Column(name = "genre")
    private Set<String> genres = new HashSet<>();

    @OneToMany(mappedBy = "tvShow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Season> seasons = new ArrayList<>();

    public TvShow(Long id, String title, String description, LocalDate firstAirDate,
                  Double rating, Long viewCount, Set<String> genres, List<Season> seasons, Boolean featured) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.firstAirDate = firstAirDate;
        this.rating = rating;
        this.viewCount = (viewCount != null) ? viewCount : 0L;
        this.genres = (genres != null) ? genres : new HashSet<>();
        this.seasons = (seasons != null) ? seasons : new ArrayList<>();
        this.featured = (featured != null) ? featured : false;
    }
}
