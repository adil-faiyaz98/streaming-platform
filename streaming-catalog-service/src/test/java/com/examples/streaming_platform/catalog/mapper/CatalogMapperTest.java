// src/test/java/com/examples/streaming_platform/catalog/mapper/CatalogMapperTest.java

package com.examples.streaming_platform.catalog.mapper;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.dto.TvShowDTO;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.model.TvShow;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CatalogMapperTest {

    private final CatalogMapper mapper = Mappers.getMapper(CatalogMapper.class);

    @Test
    void movieToMovieDTO_ShouldMapAllFields() {
        // Arrange
        Set<String> genres = new HashSet<>();
        genres.add("Action");
        genres.add("Sci-Fi");

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDescription("Test Description");
        movie.setReleaseDate(LocalDate.of(2023, 1, 1));
        movie.setDuration(120);
        movie.setPosterUrl("http://example.com/poster.jpg");
        movie.setGenres(genres);
        movie.setRating(4.5);

        // Act
        MovieDTO dto = mapper.movieToMovieDTO(movie);

        // Assert
        assertNotNull(dto);
        assertEquals(movie.getId(), dto.getId());
        assertEquals(movie.getTitle(), dto.getTitle());
        assertEquals(movie.getDescription(), dto.getDescription());
        assertEquals(movie.getReleaseDate(), dto.getReleaseDate());
        assertEquals(movie.getDuration(), dto.getDuration());
        assertEquals(movie.getPosterUrl(), dto.getPosterUrl());
        assertEquals(movie.getGenres(), dto.getGenres());
        assertEquals(movie.getRating(), dto.getRating());
    }

    @Test
    void movieDTOToMovie_ShouldMapAllFields() {
        // Arrange
        Set<String> genres = new HashSet<>();
        genres.add("Action");
        genres.add("Sci-Fi");

        MovieDTO dto = new MovieDTO();
        dto.setId(1L);
        dto.setTitle("Test Movie");
        dto.setDescription("Test Description");
        dto.setReleaseDate(LocalDate.of(2023, 1, 1));
        dto.setDuration(120);
        dto.setPosterUrl("http://example.com/poster.jpg");
        dto.setGenres(genres);
        dto.setRating(4.5);

        // Act
        Movie movie = mapper.movieDTOToMovie(dto);

        // Assert
        assertNotNull(movie);
        assertEquals(dto.getId(), movie.getId());
        assertEquals(dto.getTitle(), movie.getTitle());
        assertEquals(dto.getDescription(), movie.getDescription());
        assertEquals(dto.getReleaseDate(), movie.getReleaseDate());
        assertEquals(dto.getDuration(), movie.getDuration());
        assertEquals(dto.getPosterUrl(), movie.getPosterUrl());
        assertEquals(dto.getGenres(), movie.getGenres());
        assertEquals(dto.getRating(), movie.getRating());
    }
    
    @Test
    void updateMovieFromDTO_ShouldUpdateOnlyNonNullFields() {
        // Arrange
        Set<String> genres = new HashSet<>();
        genres.add("Action");

        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Original Title");
        movie.setDescription("Original Description");
        movie.setReleaseDate(LocalDate.of(2020, 1, 1));
        movie.setDuration(120);
        movie.setPosterUrl("http://example.com/old-poster.jpg");
        movie.setGenres(genres);
        movie.setRating(4.0);

        MovieDTO dto = new MovieDTO();
        dto.setTitle("Updated Title");
        // Only update title, leaving other fields null or unset

        // Act
        mapper.updateMovieFromDTO(dto, movie);

        // Assert
        assertEquals("Updated Title", movie.getTitle());
        assertEquals("Original Description", movie.getDescription());
        assertEquals(LocalDate.of(2020, 1, 1), movie.getReleaseDate());
        assertEquals(120, movie.getDuration());
        assertEquals("http://example.com/old-poster.jpg", movie.getPosterUrl());
        assertEquals(4.0, movie.getRating());
    }

    @Test
    void tvShowToTvShowDTO_ShouldMapAllFields() {
        // Arrange
        Set<String> genres = new HashSet<>();
        genres.add("Drama");
        
        TvShow tvShow = new TvShow();
        tvShow.setId(1L);
        tvShow.setTitle("Test Show");
        tvShow.setDescription("Test Description");
        tvShow.setFirstAirDate(LocalDate.of(2023, 1, 1));
        tvShow.setPosterUrl("http://example.com/poster.jpg");
        tvShow.setGenres(genres);
        tvShow.setRating(4.5);

        // Act
        TvShowDTO dto = mapper.tvShowToTvShowDTO(tvShow);

        // Assert
        assertNotNull(dto);
        assertEquals(tvShow.getId(), dto.getId());
        assertEquals(tvShow.getTitle(), dto.getTitle());
        assertEquals(tvShow.getDescription(), dto.getDescription());
        assertEquals(tvShow.getFirstAirDate(), dto.getFirstAirDate());
        assertEquals(tvShow.getPosterUrl(), dto.getPosterUrl());
        assertEquals(tvShow.getGenres(), dto.getGenres());
        assertEquals(tvShow.getRating(), dto.getRating());
    }
}