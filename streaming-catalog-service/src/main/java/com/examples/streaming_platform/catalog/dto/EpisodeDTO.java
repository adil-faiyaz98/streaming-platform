package com.examples.streaming_platform.catalog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * DTO representing an Episode within a Season.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDTO {

    private Long id;

    @NotNull(message = "Episode number is required.")
    @Min(value = 1, message = "Episode number must be at least 1.")
    private Integer episodeNumber;

    @NotBlank(message = "Episode title is required.")
    private String title;

    private String description;
    private String overview;

    @Min(value = 1, message = "Duration must be at least 1 minute.")
    private Integer duration;

    private LocalDate airDate;
    private String imageUrl;
    private String videoUrl;

    /** The Season ID this episode belongs to. */
    @NotNull(message = "Season ID is required.")
    private Long seasonId;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
