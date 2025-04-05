package com.examples.streaming_platform.catalog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO for movie information from external APIs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExternalMovieInfoDTO {

    private String id;
    private String title;
    private String description;
    private Integer releaseYear;
    private String director;
    private Integer runtime;
    private String posterUrl;
    private Double rating;
    private List<String> genres = new ArrayList<>();
    private List<CastMemberDTO> cast = new ArrayList<>();
    
    /**
     * DTO for cast member information.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CastMemberDTO {
        private String name;
        private String character;
        private String profileUrl;
    }
}
