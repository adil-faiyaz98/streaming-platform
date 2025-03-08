package main.java.com.examples.streaming_platform.catalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {
    private Long id;
    
    @NotBlank(message = "Title is required")
    private String title;
    
    private String description;
    
    @PastOrPresent(message = "Release date cannot be in the future")
    private LocalDate releaseDate;
    
    @Positive(message = "Duration must be positive")
    private Integer duration;
    
    private String posterUrl;
    private Set<String> genres = new HashSet<>();
    private Double rating;
}