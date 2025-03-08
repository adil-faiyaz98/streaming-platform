package com.examples.streaming_platform.catalog.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EpisodeDTO {
    
    private Long id;
    
    @NotNull(message = "Episode number is required")
    @Min(value = 1, message = "Episode number must be at least 1")
    private Integer episodeNumber;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    private String overview;
    
    private Integer duration;
    
    private LocalDate airDate;
    
    private String imageUrl;
    
    private String videoUrl;
    
    private Long seasonId; // Use Long for the ID reference, not Season
    
    private OffsetDateTime createdAt;
    
    private OffsetDateTime updatedAt;
}