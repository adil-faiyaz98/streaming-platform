package com.examples.streaming_platform.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DTO representing a TV SeriesController entity.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeriesDTO {

    private Long id;

    @NotBlank(message = "Title is required.")
    @Size(max = 255, message = "Title must be less than 255 characters.")
    private String title;

    private String description;
    private Integer startYear;
    private Integer endYear;

    private String maturityRating;
    private String imageUrl;

    @Builder.Default
    private Double averageRating = 0.0;

    @Builder.Default
    private Long viewCount = 0L;

    @Builder.Default
    private Boolean featured = false;

    @Builder.Default
    private List<SeasonDTO> seasons = new ArrayList<>();

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
