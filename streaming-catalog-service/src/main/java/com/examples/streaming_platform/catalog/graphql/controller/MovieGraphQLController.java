package com.examples.streaming_platform.catalog.graphql.controller;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MovieGraphQLController {

    private final CatalogService catalogService;

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    public MovieDTO movie(@Argument Long id) {
        log.debug("Resolving movie by ID: {}", id);
        return catalogService.getMovieById(id);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    public List<MovieDTO> movies() {
        log.debug("Resolving movies (default size: 10)");
        return catalogService.getAllMovies(PageRequest.of(0, 10)).getContent();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    public List<MovieDTO> searchMovies(@Argument String title) {
        log.debug("Searching movies by title: {}", title);
        return catalogService.searchMoviesByTitle(title, PageRequest.of(0, 10)).getContent();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    public List<MovieDTO> moviesByGenre(@Argument String genre) {
        log.debug("Resolving movies by genre: {}", genre);
        return catalogService.getMoviesByGenre(genre, PageRequest.of(0, 10)).getContent();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    public List<MovieDTO> topRatedMovies() {
        log.debug("Resolving top-rated movies");
        return catalogService.getTopRatedMovies();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    public List<MovieDTO> featuredMovies() {
        log.debug("Resolving featured movies");
        return catalogService.getFeaturedMovies();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    public Map<String, Object> moviesPage(@Argument Integer page, @Argument Integer size) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 10;

        log.debug("Resolving movies page {} with size {}", pageNumber, pageSize);
        Page<MovieDTO> moviesPage = catalogService.getAllMovies(PageRequest.of(pageNumber, pageSize));

        Map<String, Object> result = new HashMap<>();
        result.put("content", moviesPage.getContent());
        result.put("totalElements", moviesPage.getTotalElements());
        result.put("totalPages", moviesPage.getTotalPages());

        return result;
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_write:movies')")
    public MovieDTO createMovie(@Argument("input") MovieDTO movieDTO) {
        log.debug("Creating movie: {}", movieDTO);
        return catalogService.createMovie(movieDTO);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_write:movies')")
    public MovieDTO updateMovie(@Argument Long id, @Argument("input") MovieDTO movieDTO) {
        log.debug("Updating movie ID: {}, data: {}", id, movieDTO);
        return catalogService.updateMovie(id, movieDTO);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_delete:movies')")
    public Boolean deleteMovie(@Argument Long id) {
        log.debug("Deleting movie ID: {}", id);
        catalogService.deleteMovie(id);
        return true;
    }
}
