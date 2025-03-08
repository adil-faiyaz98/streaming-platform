// src/test/java/com/examples/streaming_platform/catalog/mapper/CatalogMapperTest.java

package com.examples.streaming_platform.catalog.mapper;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.model.Movie;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CatalogMapperTest {

    private final CatalogMapper mapper = Mappers.getMapper(CatalogMapper.class);

    @Test
    void shouldMapMovieToMovieDTO() {
        // Given
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDescription("Test Description");
        movie.setReleaseYear(2024);
        movie.setGenres(Set.of("Action", "Drama"));
        movie.setDirector("Test Director");

        // When
        MovieDTO movieDTO = mapper.movieToMovieDTO(movie);

        // Then
        assertNotNull(movieDTO);
        assertEquals(movie.getId(), movieDTO.getId());
        assertEquals(movie.getTitle(), movieDTO.getTitle());
        assertEquals(movie.getDescription(), movieDTO.getDescription());
        assertEquals(movie.getReleaseYear(), movieDTO.getReleaseYear());
        assertEquals(movie.getGenres(), movieDTO.getGenres());
        assertEquals(movie.getDirector(), movieDTO.getDirector());
    }

    @Test
    void shouldMapMovieDTOToMovie() {
        // Given
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("Test Movie");
        movieDTO.setDescription("Test Description");
        movieDTO.setReleaseYear(2024);
        movieDTO.setGenres(Set.of("Action", "Drama"));
        movieDTO.setDirector("Test Director");

        // When
        Movie movie = mapper.movieDTOToMovie(movieDTO);

        // Then
        assertNotNull(movie);
        assertEquals(movieDTO.getTitle(), movie.getTitle());
        assertEquals(movieDTO.getDescription(), movie.getDescription());
        assertEquals(movieDTO.getReleaseYear(), movie.getReleaseYear());
        assertEquals(movieDTO.getGenres(), movie.getGenres());
        assertEquals(movieDTO.getDirector(), movie.getDirector());
    }
}