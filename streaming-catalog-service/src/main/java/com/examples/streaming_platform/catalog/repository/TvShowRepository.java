package main.java.com.examples.streaming_platform.catalog.repository;

import com.examples.streaming_platform.catalog.model.TvShow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface TvShowRepository extends JpaRepository<TvShow, Long> {
    Page<TvShow> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    
    @Query("SELECT t FROM TvShow t JOIN t.genres g WHERE g IN :genres GROUP BY t HAVING COUNT(g) >= :minGenres")
    Page<TvShow> findByGenres(@Param("genres") Set<String> genres, @Param("minGenres") long minGenres, Pageable pageable);
    
    @Query("SELECT t FROM TvShow t JOIN t.genres g WHERE g = :genre")
    Page<TvShow> findByGenre(@Param("genre") String genre, Pageable pageable);
    
    List<TvShow> findTop10ByOrderByRatingDesc();
}