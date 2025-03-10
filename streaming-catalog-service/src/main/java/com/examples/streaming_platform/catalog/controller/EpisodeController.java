package com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.service.EpisodeService;
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
public class EpisodeController {

    private final EpisodeService episodeService;

    @GetMapping("/seasons/{seasonId}/episodes")
    @PreAuthorize("hasAuthority('SCOPE_read:episodes')")
    @Operation(summary = "Get episodes by season ID")
    public ResponseEntity<List<EpisodeDTO>> getEpisodesBySeasonId(@PathVariable Long seasonId) {
        log.debug("Fetching episodes for season ID: {}", seasonId);
        return ResponseEntity.ok(episodeService.getEpisodesBySeasonId(seasonId));
    }

    @GetMapping("/seasons/{seasonId}/episodes/paged")
    public ResponseEntity<Page<EpisodeDTO>> getEpisodesBySeasonIdPaginated(
            @PathVariable Long seasonId,
            @PageableDefault(size = 20) Pageable pageable) {
        log.debug("Fetching paginated episodes for season ID: {}", seasonId);
        return ResponseEntity.ok(episodeService.getEpisodesBySeasonIdPaginated(seasonId, pageable));
    }

    @GetMapping("/episodes/{id}")
    public ResponseEntity<EpisodeDTO> getEpisodeById(@PathVariable Long id) {
        log.debug("Fetching episode by ID: {}", id);
        return ResponseEntity.ok(episodeService.getEpisodeById(id));
    }

    @GetMapping("/seasons/{seasonId}/episodes/{episodeNumber}")
    public ResponseEntity<EpisodeDTO> getEpisodeBySeasonAndNumber(
            @PathVariable Long seasonId,
            @PathVariable Integer episodeNumber) {
        log.debug("Fetching episode for season ID: {}, episode number: {}", seasonId, episodeNumber);
        return ResponseEntity.ok(episodeService.getEpisodeBySeasonIdAndEpisodeNumber(seasonId, episodeNumber));
    }

    @PostMapping("/seasons/{seasonId}/episodes")
    @ApiResponse(responseCode = "201", description = "Episode created successfully")
    public ResponseEntity<EpisodeDTO> createEpisode(
            @PathVariable Long seasonId,
            @Valid @RequestBody EpisodeDTO episodeDTO) {
        log.debug("Creating new episode for season ID: {}", seasonId);
        EpisodeDTO created = episodeService.createEpisode(seasonId, episodeDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/episodes/{id}")
    public ResponseEntity<EpisodeDTO> updateEpisode(
            @PathVariable Long id,
            @Valid @RequestBody EpisodeDTO episodeDTO) {
        log.debug("Updating episode with ID: {}", id);
        EpisodeDTO updated = episodeService.updateEpisode(id, episodeDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/episodes/{id}")
    @ApiResponse(responseCode = "204", description = "Episode deleted successfully")
    public ResponseEntity<Void> deleteEpisode(@PathVariable Long id) {
        log.debug("Deleting episode with ID: {}", id);
        episodeService.deleteEpisode(id);
        return ResponseEntity.noContent().build();
    }
}
