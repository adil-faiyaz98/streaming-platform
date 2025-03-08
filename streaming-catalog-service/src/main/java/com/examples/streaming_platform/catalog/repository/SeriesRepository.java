package main.java.com.examples.streaming_platform.catalog.repository;

import com.examples.streaming_platform.catalog.model.Series;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeriesRepository extends JpaRepository<Series, Long> {
    
    Page<Series> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    @Query("SELECT s FROM Series s JOIN s.genres g WHERE g = :genre")
    Page<Series> findByGenre(@Param("genre") String genre, Pageable pageable);
    
    List<Series> findTop10ByOrderByAverageRatingDesc();
    
    Page<Series> findByFeaturedTrue(Pageable pageable);
    
    @Query("SELECT DISTINCT s.startYear FROM Series s ORDER BY s.startYear")
    List<Integer> findAllReleaseYears();
}