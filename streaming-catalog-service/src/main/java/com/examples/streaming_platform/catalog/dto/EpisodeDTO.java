package com.examples.streaming_platform.catalog.dto;

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
    private Integer episodeNumber;
    private String title;
    private String description;
    private String overview;
    private Integer duration;
    private LocalDate airDate;
    private String imageUrl;
    private String videoUrl;
    private Long seasonId; // This is important!
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}