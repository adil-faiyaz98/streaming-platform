// CatalogServiceTest.java
package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.repository.MovieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private CatalogMapper catalogMapper;

    @InjectMocks
    private CatalogService catalogService;

    private Movie movie;
    private MovieDTO movieDTO;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");

        movieDTO = new MovieDTO();
        movieDTO.setId(1L);
        movieDTO.setTitle("Test Movie");
    }

    @Test
    void getMovieById_Success() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(catalogMapper.movieToMovieDTO(movie)).thenReturn(movieDTO);

        MovieDTO result = catalogService.getMovieById(1L);

        assertNotNull(result);
        assertEquals(movieDTO.getId(), result.getId());
        assertEquals(movieDTO.getTitle(), result.getTitle());
    }

    @Test
    void getMovieById_NotFound() {
        when(movieRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> catalogService.getMovieById(1L));
    }

    @Test
    void createMovie_Success() {
        when(catalogMapper.movieDTOToMovie(movieDTO)).thenReturn(movie);
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);
        when(catalogMapper.movieToMovieDTO(movie)).thenReturn(movieDTO);

        MovieDTO result = catalogService.createMovie(movieDTO);

        assertNotNull(result);
        assertEquals(movieDTO.getId(), result.getId());
        assertEquals(movieDTO.getTitle(), result.getTitle());
    }
}