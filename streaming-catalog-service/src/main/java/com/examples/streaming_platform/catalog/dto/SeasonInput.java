// SeasonInput.java
package com.examples.streaming_platform.catalog.dto;

import lombok.Data;

@Data
public class SeasonInput {
    private Integer seasonNumber;
    private String title;
    private String description;
    private String imageUrl;
    private Integer releaseYear;
}