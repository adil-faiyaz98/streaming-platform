package com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;
    
    @GetMapping
    public ResponseEntity<Page<MovieDTO>> getAllMovies(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(movieService.getAllMovies(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<MovieDTO>> searchMovies(
            @RequestParam String title,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(movieService.searchMoviesByTitle(title, pageable));
    }
    
    @GetMapping("/genre/{genre}")
    public ResponseEntity<Page<MovieDTO>> getMoviesByGenre(
            @PathVariable String genre,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(movieService.getMoviesByGenre(genre, pageable));
    }
    
    @GetMapping("/top-rated")
    public ResponseEntity<List<MovieDTO>> getTopRatedMovies() {
        return ResponseEntity.ok(movieService.getTopRatedMovies());
    }
    
    @PostMapping
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return new ResponseEntity<>(movieService.createMovie(movieDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MovieDTO> updateMovie(
            @PathVariable Long id,
            @Valid @RequestBody MovieDTO movieDTO) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}