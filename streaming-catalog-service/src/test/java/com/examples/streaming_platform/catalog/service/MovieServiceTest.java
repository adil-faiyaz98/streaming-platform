package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.repository.MovieRepository;
import com.examples.streaming_platform.catalog.service.MovieService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceTest {
    
    @Mock
    private MovieRepository movieRepository;
    
    @Mock
    private CatalogMapper catalogMapper;
    
    @InjectMocks
    private MovieService movieService;
    
    private Movie movie;
    private MovieDTO movieDTO;
    
    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDescription("Test Description");
        movie.setReleaseDate(LocalDate.of(2023, 1, 1));
        movie.setDuration(120);
        movie.setGenres(new HashSet<>(Collections.singletonList("Action")));
        movie.setRating(4.5);
        
        movieDTO = new MovieDTO();
        movieDTO.setId(1L);
        movieDTO.setTitle("Test Movie");
        movieDTO.setDescription("Test Description");
        movieDTO.setReleaseDate(LocalDate.of(2023, 1, 1));
        movieDTO.setDuration(120);
        movieDTO.setGenres(new HashSet<>(Collections.singletonList("Action")));
        movieDTO.setRating(4.5);
    }
    
    @Test
    void getMovieById_ShouldReturnMovie_WhenMovieExists() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(catalogMapper.movieToMovieDTO(movie)).thenReturn(movieDTO);
        
        MovieDTO result = movieService.getMovieById(1L);
        
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Movie", result.getTitle());
        
        verify(movieRepository).findById(1L);
        verify(catalogMapper).movieToMovieDTO(movie);
    }
    
    @Test
    void getMovieById_ShouldThrowException_WhenMovieDoesNotExist() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> movieService.getMovieById(999L));
        
        verify(movieRepository).findById(999L);
        verifyNoInteractions(catalogMapper);
    }
    
    @Test
    void getAllMovies_ShouldReturnPageOfMovies() {
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));
        
        when(movieRepository.findAll(any(Pageable.class))).thenReturn(moviePage);
        when(catalogMapper.movieToMovieDTO(any(Movie.class))).thenReturn(movieDTO);
        
        Page<MovieDTO> result = movieService.getAllMovies(Pageable.unpaged());
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(movieDTO, result.getContent().get(0));
        
        verify(movieRepository).findAll(any(Pageable.class));
        verify(catalogMapper).movieToMovieDTO(any(Movie.class));
    }
    
    @Test
    void searchMoviesByTitle_ShouldReturnMatchingMovies() {
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));
        
        when(movieRepository.findByTitleContainingIgnoreCase(eq("Test"), any(Pageable.class))).thenReturn(moviePage);
        when(catalogMapper.movieToMovieDTO(any(Movie.class))).thenReturn(movieDTO);
        
        Page<MovieDTO> result = movieService.searchMoviesByTitle("Test", Pageable.unpaged());
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(movieDTO, result.getContent().get(0));
        
        verify(movieRepository).findByTitleContainingIgnoreCase(eq("Test"), any(Pageable.class));
        verify(catalogMapper).movieToMovieDTO(any(Movie.class));
    }
    
    @Test
    void getMoviesByGenre_ShouldReturnMoviesInGenre() {
        Page<Movie> moviePage = new PageImpl<>(Collections.singletonList(movie));
        
        when(movieRepository.findByGenre(eq("Action"), any(Pageable.class))).thenReturn(moviePage);
        when(catalogMapper.movieToMovieDTO(any(Movie.class))).thenReturn(movieDTO);
        
        Page<MovieDTO> result = movieService.getMoviesByGenre("Action", Pageable.unpaged());
        
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(movieDTO, result.getContent().get(0));
        
        verify(movieRepository).findByGenre(eq("Action"), any(Pageable.class));
        verify(catalogMapper).movieToMovieDTO(any(Movie.class));
    }
    
    @Test
    void getTopRatedMovies_ShouldReturnTopTenMovies() {
        when(movieRepository.findTop10ByOrderByRatingDesc()).thenReturn(Collections.singletonList(movie));
        when(catalogMapper.movieToMovieDTO(any(Movie.class))).thenReturn(movieDTO);
        
        List<MovieDTO> result = movieService.getTopRatedMovies();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(movieDTO, result.get(0));
        
        verify(movieRepository).findTop10ByOrderByRatingDesc();
        verify(catalogMapper).movieToMovieDTO(any(Movie.class));
    }
    
    @Test
    void createMovie_ShouldReturnCreatedMovie() {
        when(catalogMapper.movieDTOToMovie(movieDTO)).thenReturn(movie);
        when(movieRepository.save(movie)).thenReturn(movie);
        when(catalogMapper.movieToMovieDTO(movie)).thenReturn(movieDTO);
        
        MovieDTO result = movieService.createMovie(movieDTO);
        
        assertNotNull(result);
        assertEquals(movieDTO.getTitle(), result.getTitle());
        
        verify(catalogMapper).movieDTOToMovie(movieDTO);
        verify(movieRepository).save(movie);
        verify(catalogMapper).movieToMovieDTO(movie);
    }
    
    @Test
    void updateMovie_ShouldUpdateExistingMovie() {
        when(movieRepository.findById(1L)).thenReturn(Optional.of(movie));
        when(movieRepository.save(movie)).thenReturn(movie);
        when(catalogMapper.movieToMovieDTO(movie)).thenReturn(movieDTO);
        
        MovieDTO result = movieService.updateMovie(1L, movieDTO);
        
        assertNotNull(result);
        assertEquals(movieDTO.getTitle(), result.getTitle());
        
        verify(movieRepository).findById(1L);
        verify(catalogMapper).updateMovieFromDTO(movieDTO, movie);
        verify(movieRepository).save(movie);
        verify(catalogMapper).movieToMovieDTO(movie);
    }
    
    @Test
    void updateMovie_ShouldThrowException_WhenMovieDoesNotExist() {
        when(movieRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> movieService.updateMovie(999L, movieDTO));
        
        verify(movieRepository).findById(999L);
        verify(catalogMapper, never()).updateMovieFromDTO(any(), any());
    }
    
    @Test
    void deleteMovie_ShouldDeleteMovie_WhenMovieExists() {
        when(movieRepository.existsById(1L)).thenReturn(true);
        doNothing().when(movieRepository).deleteById(1L);
        
        assertDoesNotThrow(() -> movieService.deleteMovie(1L));
        
        verify(movieRepository).existsById(1L);
        verify(movieRepository).deleteById(1L);
    }
    
    @Test
    void deleteMovie_ShouldThrowException_WhenMovieDoesNotExist() {
        when(movieRepository.existsById(999L)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> movieService.deleteMovie(999L));
        
        verify(movieRepository).existsById(999L);
        verify(movieRepository, never()).deleteById(any());
    }
}
