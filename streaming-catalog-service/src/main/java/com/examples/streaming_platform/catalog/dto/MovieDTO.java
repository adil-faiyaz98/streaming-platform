package com.examples.streaming_platform.catalog.dto;

import com.examples.streaming_platform.catalog.model.Genre;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO representing a Movie entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private Long id;

    @NotBlank(message = "Title is required.")
    @Size(max = 255, message = "Title must be less than 255 characters.")
    private String title;

    private LocalDate releaseDate;
    private Double rating;

    @Size(max = 2000, message = "Description must be less than 2000 characters.")
    private String description;

    private Integer releaseYear;
    private String director;

    @Min(value = 1, message = "Duration must be at least 1 minute.")
    private Integer duration;

    private String maturityRating;
    private String imageUrl;
    private String videoUrl;
    private String posterUrl;
    private Float averageRating;
    private Integer viewCount;
    private Boolean featured;

    private Genre genre;

    @Builder.Default
    private Set<String> genres = new HashSet<>();

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
