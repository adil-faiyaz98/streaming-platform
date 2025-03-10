package com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.service.SeasonService;
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
public class SeasonController {

    private final SeasonService seasonService;

    @GetMapping("/tv-shows/{tvShowId}/seasons")
    @PreAuthorize("hasAuthority('SCOPE_read:seasons')")
    @Operation(summary = "Get seasons by TV show ID")
    public ResponseEntity<List<SeasonDTO>> getSeasonsByTvShowId(@PathVariable Long tvShowId) {
        log.debug("Fetching seasons for TV show ID: {}", tvShowId);
        return ResponseEntity.ok(seasonService.getSeasonsByTvShowId(tvShowId));
    }

    @GetMapping("/tv-shows/{tvShowId}/seasons/paged")
    public ResponseEntity<Page<SeasonDTO>> getSeasonsByTvShowIdPaginated(
            @PathVariable Long tvShowId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Fetching paginated seasons for TV show ID: {}, pageable: {}", tvShowId, pageable);
        return ResponseEntity.ok(seasonService.getSeasonsByTvShowIdPaginated(tvShowId, pageable));
    }

    @GetMapping("/seasons/{id}")
    public ResponseEntity<SeasonDTO> getSeasonById(@PathVariable Long id) {
        log.debug("Fetching season by ID: {}", id);
        return ResponseEntity.ok(seasonService.getSeasonById(id));
    }

    @GetMapping("/tv-shows/{tvShowId}/seasons/{seasonNumber}")
    public ResponseEntity<SeasonDTO> getSeasonByTvShowIdAndSeasonNumber(
            @PathVariable Long tvShowId,
            @PathVariable Integer seasonNumber) {
        log.debug("Fetching season for TV show ID: {}, season number: {}", tvShowId, seasonNumber);
        return ResponseEntity.ok(seasonService.getSeasonByTvShowIdAndSeasonNumber(tvShowId, seasonNumber));
    }

    @PostMapping("/tv-shows/{tvShowId}/seasons")
    @PreAuthorize("hasAuthority('SCOPE_write:seasons')")
    @ApiResponse(responseCode = "201", description = "Season created successfully")
    public ResponseEntity<SeasonDTO> createSeason(
            @PathVariable Long tvShowId,
            @Valid @RequestBody SeasonDTO seasonDTO) {
        log.debug("Creating new season for TV show ID: {}", tvShowId);
        SeasonDTO created = seasonService.createSeason(tvShowId, seasonDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/seasons/{id}")
    @PreAuthorize("hasAuthority('SCOPE_write:seasons')")
    public ResponseEntity<SeasonDTO> updateSeason(
            @PathVariable Long id,
            @Valid @RequestBody SeasonDTO seasonDTO) {
        log.debug("Updating season with ID: {}, data: {}", id, seasonDTO);
        SeasonDTO updated = seasonService.updateSeason(id, seasonDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/seasons/{id}")
    @PreAuthorize("hasAuthority('SCOPE_delete:seasons')")
    @ApiResponse(responseCode = "204", description = "Season deleted successfully")
    public ResponseEntity<Void> deleteSeason(@PathVariable Long id) {
        log.debug("Deleting season by ID: {}", id);
        seasonService.deleteSeason(id);
        return ResponseEntity.noContent().build();
    }
}
