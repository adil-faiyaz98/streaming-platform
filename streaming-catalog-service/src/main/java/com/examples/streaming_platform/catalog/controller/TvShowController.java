package com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.TvShowDTO;
import com.examples.streaming_platform.catalog.service.TvShowService;
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
@RequestMapping("/api/v1/tv-shows")
@RequiredArgsConstructor
public class TvShowController {

    private final TvShowService tvShowService;
    
    @GetMapping
    public ResponseEntity<Page<TvShowDTO>> getAllTvShows(
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tvShowService.getAllTvShows(pageable));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TvShowDTO> getTvShowById(@PathVariable Long id) {
        return ResponseEntity.ok(tvShowService.getTvShowById(id));
    }
    
    @GetMapping("/search")
    public ResponseEntity<Page<TvShowDTO>> searchTvShows(
            @RequestParam String title,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tvShowService.searchTvShowsByTitle(title, pageable));
    }
    
    @GetMapping("/genre/{genre}")
    public ResponseEntity<Page<TvShowDTO>> getTvShowsByGenre(
            @PathVariable String genre,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(tvShowService.getTvShowsByGenre(genre, pageable));
    }
    
    @GetMapping("/top-rated")
    public ResponseEntity<List<TvShowDTO>> getTopRatedTvShows() {
        return ResponseEntity.ok(tvShowService.getTopRatedTvShows());
    }
    
    @PostMapping
    public ResponseEntity<TvShowDTO> createTvShow(@Valid @RequestBody TvShowDTO tvShowDTO) {
        return new ResponseEntity<>(tvShowService.createTvShow(tvShowDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<TvShowDTO> updateTvShow(
            @PathVariable Long id,
            @Valid @RequestBody TvShowDTO tvShowDTO) {
        return ResponseEntity.ok(tvShowService.updateTvShow(id, tvShowDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTvShow(@PathVariable Long id) {
        tvShowService.deleteTvShow(id);
        return ResponseEntity.noContent().build();
    }
}