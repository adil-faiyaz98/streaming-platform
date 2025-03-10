package com.examples.streaming_platform.catalog.repository;

import com.examples.streaming_platform.catalog.model.Episode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Episode} entities.
 */
@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    /**
     * Find Episodes belonging to a given season, ordered by episode number.
     *
     * @param seasonId the season ID
     * @return a list of episodes
     */
    List<Episode> findBySeasonIdOrderByEpisodeNumber(Long seasonId);

    /**
     * Find Episodes belonging to a given season, ordered by episode number.
     *
     * @param seasonId the season ID
     * @param pageable pagination information
     * @return a list of episodes
     */
    Page<Episode> findBySeasonIdOrderByEpisodeNumber(Long seasonId, Pageable pageable);


    /**
     * Paginated query to find Episodes by season ID.
     *
     * @param seasonId the season ID
     * @param pageable pagination information
     * @return a page of episodes
     */
    Page<Episode> findBySeasonId(Long seasonId, Pageable pageable);

    /**
     * Find a single episode by its season ID and episode number.
     *
     * @param seasonId the season ID
     * @param episodeNumber the episode number
     * @return optional containing an Episode if found
     */
    Optional<Episode> findBySeasonIdAndEpisodeNumber(Long seasonId, Integer episodeNumber);
}
