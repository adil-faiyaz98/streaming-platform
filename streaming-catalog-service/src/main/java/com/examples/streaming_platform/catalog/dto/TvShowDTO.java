package com.examples.streaming_platform.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * DTO representing a generic TV Show entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TvShowDTO {

    private Long id;

    @NotBlank(message = "Title is required.")
    private String title;

    private String description;

    @PastOrPresent(message = "Let's not allow time travel")
    private LocalDate firstAirDate;

    private String posterUrl;

    @Builder.Default
    private Set<String> genres = new HashSet<>();
    private Double rating;
}
