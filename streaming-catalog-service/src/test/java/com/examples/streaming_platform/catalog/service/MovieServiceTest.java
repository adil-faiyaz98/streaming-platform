package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;

    @InjectMocks
    private MovieService movieService;

    @Test
    void getAllMovies_ShouldReturnPagedMovies() {
        // Arrange
        Movie movie1 = new Movie();
        movie1.setTitle("Test Movie 1");
        Movie movie2 = new Movie();
        movie2.setTitle("Test Movie 2");
        Page<Movie> pageMovies = new PageImpl<>(List.of(movie1, movie2));
        when(movieRepository.findAll(any(Pageable.class))).thenReturn(pageMovies);

        // Act
        Page<MovieDTO> result = movieService.getAllMovies(PageRequest.of(0, 2));

        // Assert
        assertEquals(2, result.getContent().size());
        verify(movieRepository, times(1)).findAll(any(Pageable.class));
    }
}
