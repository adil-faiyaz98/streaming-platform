package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final CatalogMapper catalogMapper;
    
    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable)
                .map(catalogMapper::movieToMovieDTO);
    }
    
    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        return catalogMapper.movieToMovieDTO(movie);
    }
    
    public Page<MovieDTO> searchMoviesByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(catalogMapper::movieToMovieDTO);
    }
    
    public Page<MovieDTO> getMoviesByGenre(String genre, Pageable pageable) {
        return movieRepository.findByGenre(genre, pageable)
                .map(catalogMapper::movieToMovieDTO);
    }
    
    public List<MovieDTO> getTopRatedMovies() {
        return movieRepository.findTop10ByOrderByRatingDesc()
                .stream()
                .map(catalogMapper::movieToMovieDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = catalogMapper.movieDTOToMovie(movieDTO);
        Movie savedMovie = movieRepository.save(movie);
        return catalogMapper.movieToMovieDTO(savedMovie);
    }
    
    @Transactional
    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        catalogMapper.updateMovieFromDTO(movieDTO, existingMovie);
        Movie updatedMovie = movieRepository.save(existingMovie);
        return catalogMapper.movieToMovieDTO(updatedMovie);
    }
    
    @Transactional
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie", "id", id);
        }
        movieRepository.deleteById(id);
    }
}