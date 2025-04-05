package com.examples.streaming_platform.catalog.controller.v1;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
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

/**
 * REST controller for movie operations (API v1).
 */
@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
@Tag(name = "Movies API v1", description = "Operations for managing movies (API v1)")
@Slf4j
public class MovieControllerV1 {

    private final CatalogService catalogService;

    /**
     * Get all movies with pagination.
     *
     * @param pageable pagination information
     * @return a page of movies
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    @Operation(summary = "Get all movies", description = "Returns a paginated list of movies")
    public ResponseEntity<Page<MovieDTO>> getAllMovies(
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        log.debug("V1 - Fetching all movies with pageable: {}", pageable);
        return ResponseEntity.ok(catalogService.getAllMovies(pageable));
    }

    /**
     * Get a movie by ID.
     *
     * @param id the movie ID
     * @return the movie
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    @Operation(summary = "Get movie by ID", description = "Returns a movie by its ID")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        log.debug("V1 - Fetching movie by ID: {}", id);
        return ResponseEntity.ok(catalogService.getMovieById(id));
    }

    /**
     * Search movies by title.
     *
     * @param title the title to search for
     * @param pageable pagination information
     * @return a page of matching movies
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    @Operation(summary = "Search movies by title", description = "Returns movies matching the title search term")
    public ResponseEntity<Page<MovieDTO>> searchMoviesByTitle(
            @RequestParam String title,
            @PageableDefault(size = 10) Pageable pageable) {
        log.debug("V1 - Searching movies by title: {}", title);
        return ResponseEntity.ok(catalogService.searchMoviesByTitle(title, pageable));
    }

    /**
     * Create a new movie.
     *
     * @param movieDTO the movie to create
     * @return the created movie
     */
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_write:movies')")
    @Operation(summary = "Create a new movie", description = "Creates a new movie and returns it")
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        log.debug("V1 - Creating movie: {}", movieDTO);
        MovieDTO created = catalogService.createMovie(movieDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Update a movie.
     *
     * @param id the movie ID
     * @param movieDTO the updated movie
     * @return the updated movie
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_write:movies')")
    @Operation(summary = "Update a movie", description = "Updates an existing movie and returns it")
    public ResponseEntity<MovieDTO> updateMovie(
            @PathVariable Long id,
            @Valid @RequestBody MovieDTO movieDTO) {
        log.debug("V1 - Updating movie with ID: {}", id);
        MovieDTO updated = catalogService.updateMovie(id, movieDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a movie.
     *
     * @param id the movie ID
     * @return no content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_delete:movies')")
    @Operation(summary = "Delete a movie", description = "Deletes a movie by its ID")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.debug("V1 - Deleting movie with ID: {}", id);
        catalogService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get top rated movies.
     *
     * @return a list of top rated movies
     */
    @GetMapping("/top-rated")
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    @Operation(summary = "Get top rated movies", description = "Returns a list of top rated movies")
    public ResponseEntity<List<MovieDTO>> getTopRatedMovies() {
        log.debug("V1 - Fetching top-rated movies");
        return ResponseEntity.ok(catalogService.getTopRatedMovies());
    }
}
