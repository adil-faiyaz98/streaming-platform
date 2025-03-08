package com.examples.streaming_platform.catalog.repository;

import com.examples.streaming_platform.catalog.model.Episode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    
    List<Episode> findBySeasonIdOrderByEpisodeNumber(Long seasonId);
    
    Page<Episode> findBySeasonId(Long seasonId, Pageable pageable);
    
    Optional<Episode> findBySeasonIdAndEpisodeNumber(Long seasonId, Integer episodeNumber);
}