package com.examples.streaming_platform.catalog.repository;

import com.examples.streaming_platform.catalog.model.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);


    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g IN :genres GROUP BY m HAVING COUNT(g) >= :minGenres")
    Page<Movie> findByGenres(@Param("genres") Set<String> genres, @Param("minGenres") long minGenres, Pageable pageable);
    
    List<Movie> findByReleaseYear(Integer year);
    
    @Query("SELECT DISTINCT m.releaseYear FROM Movie m ORDER BY m.releaseYear DESC")
    List<Integer> findAllReleaseYears();
    
    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g = :genre")
    Page<Movie> findByGenre(@Param("genre") String genre, Pageable pageable);
    
    List<Movie> findTop10ByOrderByRatingDesc();

    List<Movie> findByFeaturedTrue();

    @Modifying
    @Query("UPDATE Movie m SET m.viewCount = m.viewCount + 1 WHERE m.id = :id")
    void incrementViewCount(@Param("id") Long id);

    Page<Movie> findGenresIn(Set<String> genres, Pageable pageable);
}