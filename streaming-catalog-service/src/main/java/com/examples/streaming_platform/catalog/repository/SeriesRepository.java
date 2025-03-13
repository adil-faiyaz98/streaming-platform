package com.examples.streaming_platform.catalog.repository;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.model.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {

    Page<Series> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT s FROM Series s JOIN s.genres g WHERE LOWER(g) LIKE LOWER(CONCAT('%', :genre, '%'))")
    Page<Series> findByGenresContainingIgnoreCase(@Param("genre") String genre, Pageable pageable);

    List<Series> findTop10ByOrderByAverageRatingDesc();

    List<Series> findByFeaturedTrue();

    @Modifying
    @Transactional
    @Query("UPDATE Series s SET s.viewCount = s.viewCount + 1 WHERE s.id = :id")
    void incrementViewCount(@Param("id") Long id);

    @Query("SELECT DISTINCT s.startYear FROM Series s ORDER BY s.startYear")
    List<Integer> findDistinctStartYears();

    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g = :genre")
    Page<Movie> findByGenre(@Param("genre") String genre, Pageable pageable);
}
