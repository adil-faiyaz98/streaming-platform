package com.examples.streaming_platform.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDTO {
    private Long id;
    
    @NotNull(message = "Episode number is required")
    @Positive(message = "Episode number must be positive")
    private Integer episodeNumber;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String overview;
    
    @Positive(message = "Duration must be positive")
    private Integer duration;
    
    private LocalDate airDate;
    private String imageUrl;
    private Long seasonId;
}