package main.java.com.examples.streaming_platform.catalog.repository;

import com.examples.streaming_platform.catalog.model.Season;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Long> {
    
    List<Season> findByTvShowIdOrderBySeasonNumber(Long tvShowId);
    
    Page<Season> findByTvShowId(Long tvShowId, Pageable pageable);
    
    Optional<Season> findByTvShowIdAndSeasonNumber(Long tvShowId, Integer seasonNumber);
}