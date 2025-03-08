package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tv_shows")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TvShow {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String description;
    
    @Column(name = "first_air_date")
    private LocalDate firstAirDate;
    
    @Column(name = "poster_url")
    private String posterUrl;
    
    @ElementCollection
    @CollectionTable(name = "tv_show_genres", joinColumns = @JoinColumn(name = "tv_show_id"))
    @Column(name = "genre")
    private Set<String> genres = new HashSet<>();
    
    private Double rating;
    
    @OneToMany(mappedBy = "tvShow", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Season> seasons = new ArrayList<>();
}