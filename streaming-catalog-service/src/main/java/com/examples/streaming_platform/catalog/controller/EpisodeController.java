package com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.service.EpisodeService;
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
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EpisodeController {

    private final EpisodeService episodeService;
    
    @GetMapping("/seasons/{seasonId}/episodes")
    public ResponseEntity<List<EpisodeDTO>> getEpisodesBySeasonId(@PathVariable Long seasonId) {
        return ResponseEntity.ok(episodeService.getEpisodesBySeasonId(seasonId));
    }
    
    @GetMapping("/seasons/{seasonId}/episodes/paged")
    public ResponseEntity<Page<EpisodeDTO>> getEpisodesBySeasonIdPaginated(
            @PathVariable Long seasonId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(episodeService.getEpisodesBySeasonIdPaginated(seasonId, pageable));
    }
    
    @GetMapping("/episodes/{id}")
    public ResponseEntity<EpisodeDTO> getEpisodeById(@PathVariable Long id) {
        return ResponseEntity.ok(episodeService.getEpisodeById(id));
    }
    
    @GetMapping("/seasons/{seasonId}/episodes/{episodeNumber}")
    public ResponseEntity<EpisodeDTO> getEpisodeBySeasonIdAndEpisodeNumber(
            @PathVariable Long seasonId,
            @PathVariable Integer episodeNumber) {
        return ResponseEntity.ok(episodeService.getEpisodeBySeasonIdAndEpisodeNumber(seasonId, episodeNumber));
    }
    
    @PostMapping("/seasons/{seasonId}/episodes")
    public ResponseEntity<EpisodeDTO> createEpisode(
            @PathVariable Long seasonId,
            @Valid @RequestBody EpisodeDTO episodeDTO) {
        return new ResponseEntity<>(episodeService.createEpisode(seasonId, episodeDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/episodes/{id}")
    public ResponseEntity<EpisodeDTO> updateEpisode(
            @PathVariable Long id,
            @Valid @RequestBody EpisodeDTO episodeDTO) {
        return ResponseEntity.ok(episodeService.updateEpisode(id, episodeDTO));
    }
    
    @DeleteMapping("/episodes/{id}")
    public ResponseEntity<Void> deleteEpisode(@PathVariable Long id) {
        episodeService.deleteEpisode(id);
        return ResponseEntity.noContent().build();
    }
}