package com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.dto.TvShowDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/catalog")
@RequiredArgsConstructor
@Tag(name = "Catalog API", description = "API for managing streaming content catalog")
public class CatalogController {
    private final CatalogService catalogService;

    // Movie endpoints
    @GetMapping("/movies")
    @Operation(summary = "Get all movies", description = "Returns a paginated list of all movies")
    public ResponseEntity<Page<MovieDTO>> getAllMovies(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(catalogService.getAllMovies(pageable));
    }

    @GetMapping("/movies/{id}")
    @Operation(summary = "Get movie by ID", description = "Returns a single movie by its ID")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getMovieById(id));
    }

    @PostMapping("/movies")
    @Operation(summary = "Create a new movie", description = "Adds a new movie to the catalog")
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        return new ResponseEntity<>(catalogService.createMovie(movieDTO), HttpStatus.CREATED);
    }

    @PutMapping("/movies/{id}")
    @Operation(summary = "Update a movie", description = "Updates an existing movie in the catalog")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieDTO movieDTO) {
        return ResponseEntity.ok(catalogService.updateMovie(id, movieDTO));
    }

    @DeleteMapping("/movies/{id}")
    @Operation(summary = "Delete a movie", description = "Removes a movie from the catalog")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        catalogService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/movies/search")
    @Operation(summary = "Search movies by title", description = "Returns movies matching the search term in their title")
    public ResponseEntity<Page<MovieDTO>> searchMoviesByTitle(
            @RequestParam String title,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(catalogService.searchMoviesByTitle(title, pageable));
    }

    @GetMapping("/movies/filter")
    @Operation(summary = "Filter movies by genres", description = "Returns movies that match the specified genres")
    public ResponseEntity<Page<MovieDTO>> filterMoviesByGenres(
            @RequestParam Set<String> genres,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(catalogService.searchMoviesByGenres(genres, pageable));
    }

    @GetMapping("/movies/years")
    @Operation(summary = "Get all movie release years", description = "Returns a list of all release years for movies")
    public ResponseEntity<List<Integer>> getAllMovieReleaseYears() {
        return ResponseEntity.ok(catalogService.getAllMovieReleaseYears());
    }

    // TV Show endpoints
    @GetMapping("/tvshows")
    @Operation(summary = "Get all TV shows", description = "Returns a paginated list of all TV shows")
    public ResponseEntity<Page<TvShowDTO>> getAllTvShows(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(catalogService.getAllTvShows(pageable));
    }

    @GetMapping("/tvshows/{id}")
    @Operation(summary = "Get TV show by ID", description = "Returns a single TV show by its ID")
    public ResponseEntity<TvShowDTO> getTvShowById(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getTvShowById(id));
    }

    @PostMapping("/tvshows")
    @Operation(summary = "Create a new TV show", description = "Adds a new TV show to the catalog")
    public ResponseEntity<TvShowDTO> createTvShow(@Valid @RequestBody TvShowDTO tvShowDTO) {
        return new ResponseEntity<>(catalogService.createTvShow(tvShowDTO), HttpStatus.CREATED);
    }

    @PutMapping("/tvshows/{id}")
    @Operation(summary = "Update a TV show", description = "Updates an existing TV show in the catalog")
    public ResponseEntity<TvShowDTO> updateTvShow(@PathVariable Long id, @Valid @RequestBody TvShowDTO tvShowDTO) {
        return ResponseEntity.ok(catalogService.updateTvShow(id, tvShowDTO));
    }

    @DeleteMapping("/tvshows/{id}")
    @Operation(summary = "Delete a TV show", description = "Removes a TV show from the catalog")
    public ResponseEntity<Void> deleteTvShow(@PathVariable Long id) {
        catalogService.deleteTvShow(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tvshows/search")
    @Operation(summary = "Search TV shows by title", description = "Returns TV shows matching the search term in their title")
    public ResponseEntity<Page<TvShowDTO>> searchTvShowsByTitle(
            @RequestParam String title,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(catalogService.searchTvShowsByTitle(title, pageable));
    }

    @GetMapping("/tvshows/filter")
    @Operation(summary = "Filter TV shows by genres", description = "Returns TV shows that match the specified genres")
    public ResponseEntity<Page<TvShowDTO>> filterTvShowsByGenres(
            @RequestParam Set<String> genres,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(catalogService.searchTvShowsByGenres(genres, pageable));
    }
}