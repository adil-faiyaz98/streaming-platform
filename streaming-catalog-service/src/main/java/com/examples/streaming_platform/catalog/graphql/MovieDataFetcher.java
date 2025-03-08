package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.service.MovieService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@DgsComponent
@RequiredArgsConstructor
public class MovieDataFetcher {

    private final MovieService movieService;

    @DgsQuery
    public List<MovieDTO> movies(@InputArgument Integer page, @InputArgument Integer size) {
        int pageNum = Optional.ofNullable(page).orElse(0);
        int pageSize = Optional.ofNullable(size).orElse(20);
        return movieService.getAllMovies(PageRequest.of(pageNum, pageSize)).getContent();
    }

    @DgsQuery
    public MovieDTO movie(@InputArgument String id) {
        return movieService.getMovieById(Long.parseLong(id));
    }

    @DgsQuery
    public List<MovieDTO> moviesByTitle(@InputArgument String title, 
                                       @InputArgument Integer page, 
                                       @InputArgument Integer size) {
        int pageNum = Optional.ofNullable(page).orElse(0);
        int pageSize = Optional.ofNullable(size).orElse(20);
        return movieService.searchMoviesByTitle(title, PageRequest.of(pageNum, pageSize)).getContent();
    }

    @DgsQuery
    public List<MovieDTO> moviesByGenre(@InputArgument String genre, 
                                       @InputArgument Integer page, 
                                       @InputArgument Integer size) {
        int pageNum = Optional.ofNullable(page).orElse(0);
        int pageSize = Optional.ofNullable(size).orElse(20);
        return movieService.getMoviesByGenre(genre, PageRequest.of(pageNum, pageSize)).getContent();
    }

    @DgsQuery
    public List<MovieDTO> topRatedMovies() {
        return movieService.getTopRatedMovies();
    }

    @DgsMutation
    public MovieDTO createMovie(@InputArgument("input") MovieDTO movieDTO) {
        return movieService.createMovie(movieDTO);
    }

    @DgsMutation
    public MovieDTO updateMovie(@InputArgument String id, @InputArgument("input") MovieDTO movieDTO) {
        return movieService.updateMovie(Long.parseLong(id), movieDTO);
    }

    @DgsMutation
    public Boolean deleteMovie(@InputArgument String id) {
        movieService.deleteMovie(Long.parseLong(id));
        return true;
    }

    @DgsQuery
    public Map<String, Object> moviesPage(@InputArgument Integer page, @InputArgument Integer size) {
        int pageNum = Optional.ofNullable(page).orElse(0);
        int pageSize = Optional.ofNullable(size).orElse(20);
        
        Page<MovieDTO> moviesPage = movieService.getAllMovies(PageRequest.of(pageNum, pageSize));
        
        return Map.of(
            "content", moviesPage.getContent(),
            "totalElements", moviesPage.getTotalElements(),
            "totalPages", moviesPage.getTotalPages(),
            "size", moviesPage.getSize(),
            "number", moviesPage.getNumber()
        );
    }
}