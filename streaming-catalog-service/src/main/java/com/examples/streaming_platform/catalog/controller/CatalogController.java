package com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog")
@RequiredArgsConstructor
@Tag(name = "Catalog API", description = "Operations for managing streaming content catalog")
@Slf4j
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/movies")
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    @Operation(summary = "Get all movies", description = "Returns a paginated list of movies")
    public ResponseEntity<Page<MovieDTO>> getAllMovies(
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        log.debug("Fetching all movies with pageable: {}", pageable);
        return ResponseEntity.ok(catalogService.getAllMovies(pageable));
    }

    @GetMapping("/movies/{id}")
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        log.debug("Fetching movie by ID: {}", id);
        return ResponseEntity.ok(catalogService.getMovieById(id));
    }

    @GetMapping("/movies/search")
    public ResponseEntity<Page<MovieDTO>> searchMoviesByTitle(
            @RequestParam String title,
            @PageableDefault(size = 10) Pageable pageable) {
        log.debug("Searching movies by title: {}", title);
        return ResponseEntity.ok(catalogService.searchMoviesByTitle(title, pageable));
    }

    @PostMapping("/movies")
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        log.debug("Creating movie: {}", movieDTO);
        MovieDTO created = catalogService.createMovie(movieDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/series")
    public ResponseEntity<Page<SeriesDTO>> getAllSeries(@PageableDefault(size = 10, sort = "title") Pageable pageable) {
        log.debug("Fetching all series with pageable: {}", pageable);
        return ResponseEntity.ok(catalogService.getAllSeries(pageable));
    }

    @PostMapping("/series")
    public ResponseEntity<SeriesDTO> createSeries(@Valid @RequestBody SeriesDTO seriesDTO) {
        log.debug("Creating new series: {}", seriesDTO);
        SeriesDTO createdSeries = catalogService.createSeries(seriesDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSeries);
    }

    @DeleteMapping("/episodes/{id}")
    public ResponseEntity<Void> deleteEpisode(@PathVariable Long id) {
        log.debug("Deleting episode by ID: {}", id);
        catalogService.deleteEpisode(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/episodes/{id}")
    public ResponseEntity<EpisodeDTO> getEpisodeById(@PathVariable Long id) {
        log.debug("Fetching episode by ID: {}", id);
        EpisodeDTO episode = catalogService.getEpisodeById(id);
        return ResponseEntity.ok(episode);
    }

    @GetMapping("/movies/top-rated")
    public ResponseEntity<List<MovieDTO>> getTopRatedMovies() {
        log.debug("Fetching top-rated movies");
        return ResponseEntity.ok(catalogService.getTopRatedMovies());
    }

    @GetMapping("/series/top-rated")
    public ResponseEntity<List<SeriesDTO>> getTopRatedSeries() {
        log.debug("Fetching top-rated series");
        return ResponseEntity.ok(catalogService.getTopRatedSeries());
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.debug("Deleting movie ID: {}", id);
        catalogService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/series/{id}")
    public ResponseEntity<Void> deleteSeries(@PathVariable Long id) {
        log.debug("Deleting series with ID: {}", id);
        catalogService.deleteSeries(id);
        return ResponseEntity.noContent().build();
    }
}
