package main.java.com.examples.streaming_platform.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
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
    private String title;
    
    private String description;
    
    private Integer startYear;
    
    private Integer endYear;
    
    private String maturityRating;
    private String imageUrl;
    private Double averageRating;
    private Long viewCount;
    private Boolean featured;
    private Set<String> genres = new HashSet<>();
    private List<SeasonDTO> seasons;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}