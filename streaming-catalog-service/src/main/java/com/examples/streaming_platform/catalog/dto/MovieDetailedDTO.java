package com.examples.streaming_platform.catalog.dto;

import com.examples.streaming_platform.catalog.model.Genre;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * DTO for detailed movie information, including data from external sources.
 */
@Data
@NoArgsConstructor
public class MovieDetailedDTO {

    private Long id;
    private String title;
    private String description;
    private Integer releaseYear;
    private String director;
    private Integer duration;
    private String maturityRating;
    private String imageUrl;
    private String posterUrl;
    private Double averageRating;
    private Double externalRating;
    private Long viewCount;
    private Boolean featured;
    private Set<Genre> genres;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<ExternalMovieInfoDTO.CastMemberDTO> cast = new ArrayList<>();
    private List<String> keywords = new ArrayList<>();
    private String trailerUrl;
    private String productionCompany;
    private String country;
    private String language;
    private Double budget;
    private Double revenue;

    /**
     * Create a detailed DTO from a basic movie DTO.
     *
     * @param movieDTO the basic movie DTO
     */
    public MovieDetailedDTO(MovieDTO movieDTO) {
        this.id = movieDTO.getId();
        this.title = movieDTO.getTitle();
        this.description = movieDTO.getDescription();
        this.releaseYear = movieDTO.getReleaseYear();
        this.director = movieDTO.getDirector();
        this.duration = movieDTO.getDuration();
        this.maturityRating = movieDTO.getMaturityRating();
        this.imageUrl = movieDTO.getImageUrl();
        this.averageRating = movieDTO.getAverageRating();
        this.viewCount = movieDTO.getViewCount();
        this.featured = movieDTO.getFeatured();
        this.genres = movieDTO.getGenres();
        this.createdAt = movieDTO.getCreatedAt();
        this.updatedAt = movieDTO.getUpdatedAt();
    }
}
