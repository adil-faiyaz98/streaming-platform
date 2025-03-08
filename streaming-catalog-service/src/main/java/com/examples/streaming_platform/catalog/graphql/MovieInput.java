// graphql/fetcher/MovieDataFetcher.java
package com.examples.streaming_platform.catalog.graphql;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class MovieDataFetcher {
    private final CatalogService catalogService;

    @DgsQuery
    public MovieDTO movie(@InputArgument Long id) {
        return catalogService.getMovieById(id);
    }

    @DgsQuery
    public Page<MovieDTO> movies(@InputArgument Integer page, @InputArgument Integer size) {
        return catalogService.getAllMovies(PageRequest.of(page != null ? page : 0, size != null ? size : 10));
    }

    @DgsQuery
    public List<MovieDTO> searchMovies(@InputArgument String query) {
        return catalogService.searchMovies(query);
    }

    @DgsQuery
    public List<MovieDTO> featuredMovies() {
        return catalogService.getFeaturedMovies();
    }

    @DgsMutation
    public MovieDTO createMovie(@InputArgument MovieInput input) {
        MovieDTO movieDTO = convertInputToDTO(input);
        return catalogService.createMovie(movieDTO);
    }

    @DgsMutation
    public MovieDTO updateMovie(@InputArgument Long id, @InputArgument MovieInput input) {
        MovieDTO movieDTO = convertInputToDTO(input);
        return catalogService.updateMovie(id, movieDTO);
    }

    @DgsMutation
    public Boolean deleteMovie(@InputArgument Long id) {
        catalogService.deleteMovie(id);
        return true;
    }

    private MovieDTO convertInputToDTO(MovieInput input) {
        MovieDTO dto = new MovieDTO();
        dto.setTitle(input.getTitle());
        dto.setDescription(input.getDescription());
        dto.setReleaseYear(input.getReleaseYear());
        dto.setGenres(input.getGenres());
        dto.setDirector(input.getDirector());
        dto.setDuration(input.getDuration());
        dto.setMaturityRating(input.getMaturityRating());
        dto.setImageUrl(input.getImageUrl());
        dto.setVideoUrl(input.getVideoUrl());
        dto.setFeatured(input.getFeatured());
        return dto;
    }
}