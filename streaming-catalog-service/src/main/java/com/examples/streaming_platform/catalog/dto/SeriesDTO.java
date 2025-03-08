package com.examples.streaming_platform.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeriesDTO {
    
    private Long id;
    
    @NotBlank(message = "Title is required")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;
    
    private String description;
    
    private Integer startYear;
    
    private Integer endYear;
    
    private Set<String> genres = new HashSet<>();
    
    private String maturityRating;
    
    private String imageUrl;
    
    private Double averageRating = 0.0;
    
    private Long viewCount = 0L;
    
    private Boolean featured = false;
    
    private List<SeasonDTO> seasons;
    
    private OffsetDateTime createdAt;
    
    private OffsetDateTime updatedAt;
}