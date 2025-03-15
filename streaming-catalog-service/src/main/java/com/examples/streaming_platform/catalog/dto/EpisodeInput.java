// EpisodeInput.java
package com.examples.streaming_platform.catalog.dto;

import lombok.Data;

@Data
public class EpisodeInput {
    private Integer episodeNumber;
    private String title;
    private String description;
    private Integer duration;
    private String imageUrl;
    private String videoUrl;
}