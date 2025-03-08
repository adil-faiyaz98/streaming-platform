package com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.service.SeasonService;
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
public class SeasonController {

    private final SeasonService seasonService;
    
    @GetMapping("/tv-shows/{tvShowId}/seasons")
    public ResponseEntity<List<SeasonDTO>> getSeasonsByTvShowId(@PathVariable Long tvShowId) {
        return ResponseEntity.ok(seasonService.getSeasonsByTvShowId(tvShowId));
    }
    
    @GetMapping("/tv-shows/{tvShowId}/seasons/paged")
    public ResponseEntity<Page<SeasonDTO>> getSeasonsByTvShowIdPaginated(
            @PathVariable Long tvShowId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(seasonService.getSeasonsByTvShowIdPaginated(tvShowId, pageable));
    }
    
    @GetMapping("/seasons/{id}")
    public ResponseEntity<SeasonDTO> getSeasonById(@PathVariable Long id) {
        return ResponseEntity.ok(seasonService.getSeasonById(id));
    }
    
    @GetMapping("/tv-shows/{tvShowId}/seasons/{seasonNumber}")
    public ResponseEntity<SeasonDTO> getSeasonByTvShowIdAndSeasonNumber(
            @PathVariable Long tvShowId,
            @PathVariable Integer seasonNumber) {
        return ResponseEntity.ok(seasonService.getSeasonByTvShowIdAndSeasonNumber(tvShowId, seasonNumber));
    }
    
    @PostMapping("/tv-shows/{tvShowId}/seasons")
    public ResponseEntity<SeasonDTO> createSeason(
            @PathVariable Long tvShowId,
            @Valid @RequestBody SeasonDTO seasonDTO) {
        return new ResponseEntity<>(seasonService.createSeason(tvShowId, seasonDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/seasons/{id}")
    public ResponseEntity<SeasonDTO> updateSeason(
            @PathVariable Long id,
            @Valid @RequestBody SeasonDTO seasonDTO) {
        return ResponseEntity.ok(seasonService.updateSeason(id, seasonDTO));
    }
    
    @DeleteMapping("/seasons/{id}")
    public ResponseEntity<Void> deleteSeason(@PathVariable Long id) {
        seasonService.deleteSeason(id);
        return ResponseEntity.noContent().build();
    }
}