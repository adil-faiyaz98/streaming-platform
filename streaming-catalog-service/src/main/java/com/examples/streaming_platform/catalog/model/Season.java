// model/Season.java
package com.examples.streaming_platform.catalog.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @OneToMany(mappedBy = "season", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Episode> episodes = new ArrayList<>();
}