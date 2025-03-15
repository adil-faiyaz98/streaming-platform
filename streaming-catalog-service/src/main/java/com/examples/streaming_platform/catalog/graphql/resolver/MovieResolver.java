package com.examples.streaming_platform.catalog.graphql.resolver;

import com.examples.streaming_platform.catalog.model.Genre;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * A GraphQL resolver for Movie queries & mutations.
 */
@RequiredArgsConstructor
@Slf4j
public class MovieResolver{

    private final MovieService movieService;

    // Queries

    public Movie getMovie(Long id) {
        log.debug("Resolving movie by ID: {}", id);
        return movieService.getMovieById(id);
    }

    /**
     * Returns a pageable result of movies, optionally filtered by title or genre.
     * Could be used in a GraphQL field like: movies(page: Int, size: Int, title: String, genre: String).
     */
    public Map<String, Object> getMovies(Integer page, Integer size, Optional<String> title, Optional<Genre> genre) {
        int pageNumber = (page != null) ? page : 0;
        int pageSize = (size != null) ? size : 20;
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        Page<Movie> moviePage;

        if (title.isPresent() && !title.get().isBlank()) {
            log.debug("Resolving movies by title: '{}'", title.get());
            moviePage = movieService.searchMoviesByTitle(title.get(), pageRequest);
        } else if (genre.isPresent()) {
            log.debug("Resolving movies by genre: '{}'", genre.get());
            moviePage = movieService.getMoviesByGenre(genre.toString(), pageRequest);
        } else {
            log.debug("Resolving all movies. Page: {}, Size: {}", pageNumber, pageSize);
            moviePage = movieService.getAllMovies(pageRequest);
        }

        return Map.of(
                "content", moviePage.getContent(),
                "totalElements", moviePage.getTotalElements(),
                "totalPages", moviePage.getTotalPages(),
                "size", moviePage.getSize(),
                "number", moviePage.getNumber()
        );
    }

    public List<Movie> getTopRatedMovies() {
        log.debug("Resolving top-rated movies");
        return movieService.getTopRatedMovies();
    }

    // Mutations
    public Movie createMovie(Movie movieInput) {
        log.debug("Creating movie: {}", movieInput);
        return movieService.createMovie(movieInput);
    }

    public Movie updateMovie(Long id, Movie movieInput) {
        log.debug("Updating movie ID: {}, data: {}", id, movieInput);
        return movieService.updateMovie(id, movieInput);
    }

    public Boolean deleteMovie(Long id) {
        log.debug("Deleting movie ID: {}", id);
        movieService.deleteMovie(id);
        return true;
    }
}
