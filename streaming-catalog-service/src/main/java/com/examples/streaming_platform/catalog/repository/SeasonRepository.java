package com.examples.streaming_platform.catalog.repository;

import com.examples.streaming_platform.catalog.model.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for managing {@link Season} entities.
 */
@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {

    /**
     * Find seasons by series ID, ordered by season number ascending.
     *
     * @param seriesId the ID of the series
     * @return a list of seasons
     */
    List<Season> findBySeriesIdOrderBySeasonNumber(Long seriesId);

    /**
     * Find seasons by TV show ID, ordered by season number ascending.
     *
     * @param tvShowId the ID of the TV show
     * @return a list of seasons
     */
    List<Season> findByTvShowIdOrderBySeasonNumber(Long tvShowId);

    /**
     * Paginated query to find seasons by TV show ID.
     */
    Page<Season> findByTvShowId(Long tvShowId, Pageable pageable);

    /**
     * Find a single season by its TV show ID and season number.
     */
    Optional<Season> findByTvShowIdAndSeasonNumber(Long tvShowId, Integer seasonNumber);

    /**
     * Find a single season by its SeriesController ID and season number.
     */
    Optional<Season> findBySeriesIdAndSeasonNumber(Long seriesId, Integer seasonNumber);
}
