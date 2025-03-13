package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.graphql.exception.DgsEntityNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.Genre;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MovieService {

    @Autowired
    private final MovieRepository movieRepository;
    @Autowired
    private CatalogMapper catalogMapper;

    public Page<Movie> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable);
    }

    public Movie getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new DgsEntityNotFoundException("Movie not found with id: " + id));
    }

    public Movie createMovie(Movie movie) {
        validateGenres(movie.getGenres());
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie updatedMovie) {
        Movie existing = getMovieById(id);
        existing.setTitle(updatedMovie.getTitle());
        existing.setDescription(updatedMovie.getDescription());
        existing.setDuration(updatedMovie.getDuration());
        existing.setDirector(updatedMovie.getDirector());
        existing.setLanguage(updatedMovie.getLanguage());
        existing.setCountryOfOrigin(updatedMovie.getCountryOfOrigin());
        existing.setSubtitleUrl(updatedMovie.getSubtitleUrl());
        existing.setAwards(updatedMovie.getAwards());
        existing.setBudget(updatedMovie.getBudget());
        existing.setBoxOffice(updatedMovie.getBoxOffice());
        existing.setGenres(updatedMovie.getGenres());
        validateGenres(existing.getGenres());
        return movieRepository.save(existing);
    }

    public void deleteMovie(Long id) {
        Movie existing = getMovieById(id);
        movieRepository.delete(existing);
    }

    /**
     * Filter movies by optional fields (director, language, etc.) plus optional genre set
     */
    public Page<Movie> filterMovies(String director,
                                    String language,
                                    String countryOfOrigin,
                                    Integer minDuration,
                                    Integer maxDuration,
                                    Set<Genre> genres,
                                    Pageable pageable) {

        MovieSpecification spec = new MovieSpecification(director, language, countryOfOrigin,
                minDuration, maxDuration, genres);

        return movieRepository.findAll(spec, pageable);
    }

    public List<Movie> getTopRatedMovies() {
        return movieRepository.findTop10ByOrderByAverageRatingDesc();
    }

    public Page<Movie> getMoviesByGenre(String genre, Pageable pageable) {
        return movieRepository.findByGenresContainingIgnoreCase(genre, pageable);
    }

    public Page<Movie> searchMoviesByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    /**
     * Example: Ensure at least one genre is present or throw an exception.
     */
    private void validateGenres(Set<Genre> genres) {
        if (genres == null || genres.isEmpty()) {
            throw new IllegalArgumentException("At least one genre is required for a movie.");
        }
    }
}
