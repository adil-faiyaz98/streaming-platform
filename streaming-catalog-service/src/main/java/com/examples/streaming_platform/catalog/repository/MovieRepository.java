package com.examples.streaming_platform.catalog.repository;

import com.examples.streaming_platform.catalog.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long>, JpaSpecificationExecutor<Movie> {

    // Searching by title
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Searching by featured
    List<Movie> findByFeaturedTrue();

    // Top 10 by average rating
    List<Movie> findTop10ByOrderByAverageRatingDesc();

    // Or top 10 by rating if you prefer
    List<Movie> findTop10ByOrderByRatingDesc();

    @Modifying
    @Query("UPDATE Movie m SET m.viewCount = m.viewCount + 1 WHERE m.id = :id")
    void incrementViewCount(@Param("id") Long id);

    Page<Movie> findMoviesByGenre(String genre, Pageable pageable);

    Page<Movie> findByGenresContainingIgnoreCase(String genre, Pageable pageable);

    @Query("SELECT m FROM Movie m ORDER BY m.rating DESC")
    List<Movie> getTopRatedMovies();


}
