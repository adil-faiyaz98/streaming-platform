package com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.TvShowDTO;
import com.examples.streaming_platform.catalog.service.TvShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
 * Manages operations for high-level TV Show entities.
 */
@RestController
@RequestMapping("/api/v1/tv-shows")
@RequiredArgsConstructor
@Slf4j
public class TvShowController {

    private final TvShowService tvShowService;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_read:tv-shows')")
    @Operation(summary = "Get all TV shows")
    public ResponseEntity<Page<TvShowDTO>> getAllTvShows(
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Fetching paginated TV shows, pageable: {}", pageable);
        return ResponseEntity.ok(tvShowService.getAllTvShows(pageable));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_read:tv-shows')")
    @Operation(summary = "Get TV show by ID")
    public ResponseEntity<TvShowDTO> getTvShowById(@PathVariable Long id) {
        log.debug("Fetching TV show by ID: {}", id);
        return ResponseEntity.ok(tvShowService.getTvShowById(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SCOPE_read:tv-shows')")
    @Operation(summary = "Search TV shows by title")
    public ResponseEntity<Page<TvShowDTO>> searchTvShows(
            @RequestParam String title,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Searching TV shows by title: {}", title);
        return ResponseEntity.ok(tvShowService.searchTvShowsByTitle(title, pageable));
    }

    @GetMapping("/genre/{genre}")
    @PreAuthorize("hasAuthority('SCOPE_read:tv-shows')")
    @Operation(summary = "Get TV shows by genre")
    public ResponseEntity<Page<TvShowDTO>> getTvShowsByGenre(
            @PathVariable String genre,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Fetching TV shows by genre: {}", genre);
        return ResponseEntity.ok(tvShowService.getTvShowsByGenre(genre, pageable));
    }

    @GetMapping("/top-rated")
    @PreAuthorize("hasAuthority('SCOPE_read:tv-shows')")
    @Operation(summary = "Get top-rated TV shows")
    public ResponseEntity<List<TvShowDTO>> getTopRatedTvShows() {
        log.debug("Fetching top-rated TV shows");
        return ResponseEntity.ok(tvShowService.getTopRatedTvShows());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_write:tv-shows')")
    @Operation(summary = "Create a new TV show")
    @ApiResponse(responseCode = "201", description = "TV show created successfully")
    public ResponseEntity<TvShowDTO> createTvShow(@Valid @RequestBody TvShowDTO tvShowDTO) {
        log.debug("Creating TV show: {}", tvShowDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(tvShowService.createTvShow(tvShowDTO));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_write:tv-shows')")
    @Operation(summary = "Update existing TV show")
    public ResponseEntity<TvShowDTO> updateTvShow(
            @PathVariable Long id,
            @Valid @RequestBody TvShowDTO tvShowDTO) {
        log.debug("Updating TV show ID: {}, Data: {}", id, tvShowDTO);
        return ResponseEntity.ok(tvShowService.updateTvShow(id, tvShowDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SCOPE_delete:tv-shows')")
    @Operation(summary = "Delete a TV show")
    @ApiResponse(responseCode = "204", description = "TV show deleted successfully")
    public ResponseEntity<Void> deleteTvShow(@PathVariable Long id) {
        log.debug("Deleting TV show ID: {}", id);
        tvShowService.deleteTvShow(id);
        return ResponseEntity.noContent().build();
    }
}
