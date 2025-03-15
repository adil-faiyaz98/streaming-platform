// SeriesInput.java
package com.examples.streaming_platform.catalog.dto;

import java.util.Set;

import com.examples.streaming_platform.catalog.model.Genre;
import lombok.Data;

@Data
public class SeriesInput {
    private String title;
    private String description;
    private Integer startYear;
    private Integer endYear;
    private String maturityRating;
    private String imageUrl;
    private Boolean featured;
    private Set<Genre> genres;
}