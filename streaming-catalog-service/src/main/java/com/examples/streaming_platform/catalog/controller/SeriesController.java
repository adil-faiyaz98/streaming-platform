package com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.service.SeriesService;
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

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class SeriesController {

    private final SeriesService seriesService;

    @GetMapping("/series")
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    @Operation(summary = "Get all series", description = "Returns a paginated list of series.")
    public ResponseEntity<Page<SeriesDTO>> getAllSeries(Pageable pageable) {
        log.debug("Fetching all series paginated: {}", pageable);
        return ResponseEntity.ok(seriesService.getAllSeries(pageable));
    }

    @GetMapping("/series/{id}")
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    @Operation(summary = "Get series by ID")
    public ResponseEntity<SeriesDTO> getSeriesById(@PathVariable Long id) {
        log.debug("Fetching series by ID: {}", id);
        SeriesDTO series = seriesService.getSeriesById(id);
        return ResponseEntity.ok(series);
    }

    @GetMapping("/series/featured")
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    @Operation(summary = "Get featured series")
    public ResponseEntity<List<SeriesDTO>> getFeaturedSeries() {
        log.debug("Fetching featured series");
        return ResponseEntity.ok(seriesService.getFeaturedSeries());
    }

    @GetMapping("/series/genre/{genre}")
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    @Operation(summary = "Get series by genre")
    public ResponseEntity<Page<SeriesDTO>> getSeriesByGenre(
            @PathVariable String genre,
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        log.debug("Fetching series by genre: {}", genre);
        return ResponseEntity.ok(seriesService.getSeriesByGenre(genre, pageable));
    }

    @PostMapping("/series")
    @PreAuthorize("hasAuthority('SCOPE_write:series')")
    @Operation(summary = "Create new series")
    public ResponseEntity<SeriesDTO> createSeries(@Valid @RequestBody SeriesDTO seriesDTO) {
        log.debug("Creating series: {}", seriesDTO.getTitle());
        SeriesDTO createdSeries = seriesService.createSeries(seriesDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSeries);
    }

    @PutMapping("/series/{id}")
    @PreAuthorize("hasAuthority('SCOPE_write:series')")
    @Operation(summary = "Update existing series")
    public ResponseEntity<SeriesDTO> updateSeries(@PathVariable Long id, @Valid @RequestBody SeriesDTO seriesDTO) {
        log.debug("Updating series ID: {}", id);
        SeriesDTO updatedSeries = seriesService.updateSeries(id, seriesDTO);
        return ResponseEntity.ok(updatedSeries);
    }

    @DeleteMapping("/series/{id}")
    @PreAuthorize("hasAuthority('SCOPE_delete:series')")
    @ApiResponse(responseCode = "204", description = "Series deleted successfully")
    public ResponseEntity<Void> deleteSeries(@PathVariable Long id) {
        log.debug("Deleting series ID: {}", id);
        seriesService.deleteSeries(id);
        return ResponseEntity.noContent().build();
    }
}
