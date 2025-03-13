package com.examples.streaming_platform.catalog.repository;

import com.examples.streaming_platform.catalog.model.TvShow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TvShowRepository extends JpaRepository<TvShow, Long> {

    /**
     * Find TV shows by partial title (case-insensitive).
     */
    Page<TvShow> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    /**
     * Find TV shows by exact genre match (case-insensitive).
     */
    @Query("SELECT t FROM TvShow t JOIN t.genres g WHERE LOWER(g) = LOWER(:genre)")
    Page<TvShow> findByGenresContainingIgnoreCase(String genre, Pageable pageable);

    /**
     * Find TV shows by genre.
     */
    @Query("SELECT t FROM TvShow t JOIN t.genres g WHERE g = :genre")
    Page<TvShow> findByGenre(String genre, Pageable pageable);

    /**
     * Find top 10 TV shows ordered by rating descending.
     */
    List<TvShow> findTop10ByOrderByRatingDesc();

    /**
     * Check if TvShow exists by title.
     */
    boolean existsByTitleIgnoreCase(String title);

    /**
     * Find all featured TV shows.
     */
    List<TvShow> findByFeaturedTrue();

    /**
     * Find featured TV shows with rating greater than 8.5.
     */
    @Query("SELECT t FROM TvShow t WHERE t.rating > 8.5")
    List<TvShow> findFeaturedShows();

    /**
     * Find TV shows by multiple genres with minimum genre match count.
     */
    @Query("SELECT t FROM TvShow t JOIN t.genres g WHERE g IN :genres GROUP BY t HAVING COUNT(g) >= :minGenres")
    Page<TvShow> findByGenresWithMinMatch(List<String> genres, long minGenres, Pageable pageable);

    /**
     * Check existence by ID (already available by JpaRepository, but explicitly included for clarity).
     */
    boolean existsById(Long id);
}
