package main.java.com.examples.streaming_platform.catalog.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeasonDTO {
    private Long id;
    
    @NotNull(message = "Season number is required")
    @Positive(message = "Season number must be positive")
    private Integer seasonNumber;
    
    private String title;
    private String overview;
    private String posterUrl;
    private LocalDate airDate;
    private Long tvShowId;
    private Integer releaseYear;
    private List<EpisodeDTO> episodes = new ArrayList<>();
}