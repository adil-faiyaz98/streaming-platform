package com.examples.streaming_platform.catalog.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO representing a Season of a TV series.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeasonDTO {

    private Long id;

    @NotNull(message = "Season number is required.")
    @Positive(message = "Season number must be positive.")
    private Integer seasonNumber;

    @NotNull(message = "Season ID is required.")
    private Long seasonId;

    private String imageUrl;

    private String description;

    private String title;
    private String overview;
    private String posterUrl;
    private LocalDate airDate;

    private String seriesId;
    private Long tvShowId;

    private Integer releaseYear;

    /** List of episodes for this season. */
    @Builder.Default
    private List<EpisodeDTO> episodes = new ArrayList<>();
}
