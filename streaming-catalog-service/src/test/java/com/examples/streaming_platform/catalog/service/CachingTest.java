package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
class CachingTest {

    @MockBean
    private MovieRepository movieRepository;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private CacheManager cacheManager;

    @Test
    void getMovieById_ShouldUseCaching() {
        // Given
        Long movieId = 1L;
        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setTitle("Cached Movie");
        
        when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

        // When
        // First call should hit the repository
        catalogService.getMovieById(movieId);
        
        // Second call should use cache
        catalogService.getMovieById(movieId);
        
        // Third call should use cache
        catalogService.getMovieById(movieId);

        // Then
        // Repository should be called only once
        verify(movieRepository, times(1)).findById(movieId);
    }

    @Test
    void getAllMovies_ShouldUseCaching() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Cached Movie");
        
        when(movieRepository.findAll(eq(pageable)))
                .thenReturn(new PageImpl<>(Collections.singletonList(movie)));

        // When
        // First call should hit the repository
        catalogService.getAllMovies(pageable);
        
        // Second call with same pageable should use cache
        catalogService.getAllMovies(pageable);

        // Then
        // Repository should be called only once
        verify(movieRepository, times(1)).findAll(eq(pageable));
    }

    @Test
    void createMovie_ShouldEvictCache() {
        // Given
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("New Movie");
        
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("New Movie");
        
        when(movieRepository.save(any(Movie.class))).thenReturn(movie);

        // Populate cache
        when(movieRepository.findAll(any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.emptyList()));
        catalogService.getAllMovies(PageRequest.of(0, 10));

        // When
        // Creating a new movie should evict the cache
        catalogService.createMovie(movieDTO);

        // Then
        // Next getAllMovies call should hit the repository again
        catalogService.getAllMovies(PageRequest.of(0, 10));
        
        // Repository's findAll should be called twice
        verify(movieRepository, times(2)).findAll(any(Pageable.class));
    }
}
