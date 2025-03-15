// MovieInput.java
package com.examples.streaming_platform.catalog.dto;

import java.util.Set;

import com.examples.streaming_platform.catalog.model.Genre;
import lombok.Data;

@Data
public class MovieInput {
    private String title;
    private String description;
    private Integer releaseYear;
    private String director;
    private Integer duration;
    private String maturityRating;
    private String imageUrl;
    private String videoUrl;
    private Boolean featured;
    private Set<Genre> genres;
}