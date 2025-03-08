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
    
    List<Series> findByFeaturedTrue();
    
    @Query("SELECT g.genres as genre, COUNT(g) as count FROM Series s JOIN s.genres g GROUP BY g.genres ORDER BY COUNT(g) DESC")
    List<Object[]> getGenreStats();
}