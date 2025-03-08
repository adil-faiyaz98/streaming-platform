package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MovieGraphQLController {

    @Autowired
    private CatalogService catalogService;

    @QueryMapping
    public MovieDTO movie(@Argument String id) {
        return catalogService.getMovieById(Long.valueOf(id));
    }

    @QueryMapping
    public List<MovieDTO> movies() {
        // Return a reasonable default size
        return catalogService.getAllMovies(PageRequest.of(0, 10)).getContent();
    }

    @QueryMapping
    public List<MovieDTO> searchMovies(@Argument String title) {
        return catalogService.searchMoviesByTitle(title, PageRequest.of(0, 10)).getContent();
    }

    @QueryMapping
    public List<MovieDTO> moviesByGenre(@Argument String genre) {
        return catalogService.getMoviesByGenre(genre, PageRequest.of(0, 10)).getContent();
    }

    @QueryMapping
    public List<MovieDTO> topRatedMovies() {
        return catalogService.getTopRatedMovies();
    }

    @QueryMapping
    public List<MovieDTO> featuredMovies() {
        return catalogService.getFeaturedMovies();
    }

    @MutationMapping
    public MovieDTO createMovie(@Argument("input") MovieDTO movieDTO) {
        return catalogService.createMovie(movieDTO);
    }

    @MutationMapping
    public MovieDTO updateMovie(@Argument String id, @Argument("input") MovieDTO movieDTO) {
        return catalogService.updateMovie(Long.valueOf(id), movieDTO);
    }

    @MutationMapping
    public Boolean deleteMovie(@Argument String id) {
        catalogService.deleteMovie(Long.valueOf(id));
        return true;
    }

    @QueryMapping
    public Map<String, Object> moviesPage(@Argument Integer page, @Argument Integer size) {
        page = page == null ? 0 : page;
        size = size == null ? 10 : size;
        
        Page<MovieDTO> moviesPage = catalogService.getAllMovies(PageRequest.of(page, size));
        
        Map<String, Object> result = new HashMap<>();
        result.put("content", moviesPage.getContent());
        result.put("totalElements", moviesPage.getTotalElements());
        result.put("totalPages", moviesPage.getTotalPages());
        
        return result;
    }
}