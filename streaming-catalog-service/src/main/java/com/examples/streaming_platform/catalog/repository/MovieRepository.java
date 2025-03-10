package com.examples.streaming_platform.catalog.repository;

import com.examples.streaming_platform.catalog.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for CRUD and specialized queries on Movie entities.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Search movies by title (case-insensitive).
     */
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    /**
     * Example for searching movies by a single genre.
     * If using an enum field, you might do: findByGenresIn(Set<Genre> genres, Pageable p).
     */
    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE lower(g) = lower(:genre)")
    Page<Movie> findByGenre(@Param("genre") String genre, Pageable pageable);

    /**
     * Retrieve top 10 movies by highest rating (descending).
     */
    List<Movie> findTop10ByOrderByRatingDesc();
}
