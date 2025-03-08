package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.model.Series;
import com.examples.streaming_platform.catalog.model.Season;
import com.examples.streaming_platform.catalog.model.Episode;
import com.examples.streaming_platform.catalog.repository.MovieRepository;
import com.examples.streaming_platform.catalog.repository.SeriesRepository;
import com.examples.streaming_platform.catalog.repository.SeasonRepository;
import com.examples.streaming_platform.catalog.repository.EpisodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Provides catalog-related functionality for the streaming platform.
 * Handles operations for movies, series, seasons, and episodes.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CatalogService {

    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;
    private final CatalogMapper catalogMapper;

    // Movie related methods
    
    @Cacheable(value = "movies", key = "'page_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        log.debug("Fetching all movies with pagination: {}", pageable);
        return movieRepository.findAll(pageable)
                .map(catalogMapper::movieToMovieDTO);
    }
    
    @Cacheable(value = "movies", key = "'id_' + #id")
    public MovieDTO getMovieById(Long id) {
        log.debug("Fetching movie with id: {}", id);
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        return catalogMapper.movieToMovieDTO(movie);
    }
    
    public Page<MovieDTO> searchMoviesByTitle(String title, Pageable pageable) {
        log.debug("Searching movies with title containing: {}", title);
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(catalogMapper::movieToMovieDTO);
    }
    
    public Page<MovieDTO> getMoviesByGenre(String genre, Pageable pageable) {
        log.debug("Fetching movies with genre: {}", genre);
        return movieRepository.findByGenre(genre, pageable)
                .map(catalogMapper::movieToMovieDTO);
    }
    
    @Cacheable(value = "topRatedMovies")
    public List<MovieDTO> getTopRatedMovies() {
        log.debug("Fetching top rated movies");
        return movieRepository.findTop10ByOrderByRatingDesc().stream()
                .map(catalogMapper::movieToMovieDTO)
                .collect(Collectors.toList());
    }
    
    public List<MovieDTO> getFeaturedMovies() {
        log.debug("Fetching featured movies");
        return movieRepository.findByFeaturedTrue().stream()
                .map(catalogMapper::movieToMovieDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    @CacheEvict(value = {"movies", "topRatedMovies"}, allEntries = true)
    public MovieDTO createMovie(MovieDTO movieDTO) {
        log.debug("Creating new movie: {}", movieDTO.getTitle());
        Movie movie = catalogMapper.movieDTOToMovie(movieDTO);
        Movie savedMovie = movieRepository.save(movie);
        return catalogMapper.movieToMovieDTO(savedMovie);
    }
    
    @Transactional
    @CacheEvict(value = {"movies", "topRatedMovies"}, allEntries = true)
    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
        log.debug("Updating movie with id: {}", id);
        Movie existingMovie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        catalogMapper.updateMovieFromDTO(movieDTO, existingMovie);
        Movie updatedMovie = movieRepository.save(existingMovie);
        return catalogMapper.movieToMovieDTO(updatedMovie);
    }
    
    @Transactional
    @CacheEvict(value = {"movies", "topRatedMovies"}, allEntries = true)
    public void deleteMovie(Long id) {
        log.debug("Deleting movie with id: {}", id);
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie", "id", id);
        }
        movieRepository.deleteById(id);
    }
    
    public void incrementMovieViewCount(Long id) {
        log.debug("Incrementing view count for movie with id: {}", id);
        movieRepository.incrementViewCount(id);
    }

    // Series related methods
    
    @Cacheable(value = "series", key = "'page_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<SeriesDTO> getAllSeries(Pageable pageable) {
        log.debug("Fetching all series with pagination: {}", pageable);
        return seriesRepository.findAll(pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }
    
    @Cacheable(value = "series", key = "'id_' + #id")
    public SeriesDTO getSeriesById(Long id) {
        log.debug("Fetching series with id: {}", id);
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Series", "id", id));
        return catalogMapper.seriesToSeriesDTO(series);
    }
    
    public Page<SeriesDTO> searchSeriesByTitle(String title, Pageable pageable) {
        log.debug("Searching series with title containing: {}", title);
        return seriesRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }
    
    public Page<SeriesDTO> getSeriesByGenre(String genre, Pageable pageable) {
        log.debug("Fetching series with genre: {}", genre);
        return seriesRepository.findByGenre(genre, pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }
    
    @Cacheable(value = "topRatedSeries")
    public List<SeriesDTO> getTopRatedSeries() {
        log.debug("Fetching top rated series");
        return seriesRepository.findTop10ByOrderByAverageRatingDesc().stream()
                .map(catalogMapper::seriesToSeriesDTO)
                .collect(Collectors.toList());
    }
    
    public List<SeriesDTO> getFeaturedSeries() {
        log.debug("Fetching featured series");
        return seriesRepository.findByFeaturedTrue().stream()
                .map(catalogMapper::seriesToSeriesDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    @CacheEvict(value = {"series", "topRatedSeries"}, allEntries = true)
    public SeriesDTO createSeries(SeriesDTO seriesDTO) {
        log.debug("Creating new series: {}", seriesDTO.getTitle());
        Series series = catalogMapper.seriesDTOToSeries(seriesDTO);
        Series savedSeries = seriesRepository.save(series);
        return catalogMapper.seriesToSeriesDTO(savedSeries);
    }
    
    @Transactional
    @CacheEvict(value = {"series", "topRatedSeries"}, allEntries = true)
    public SeriesDTO updateSeries(Long id, SeriesDTO seriesDTO) {
        log.debug("Updating series with id: {}", id);
        Series existingSeries = seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Series", "id", id));
        
        catalogMapper.updateSeriesFromDTO(seriesDTO, existingSeries);
        Series updatedSeries = seriesRepository.save(existingSeries);
        return catalogMapper.seriesToSeriesDTO(updatedSeries);
    }
    
    @Transactional
    @CacheEvict(value = {"series", "topRatedSeries"}, allEntries = true)
    public void deleteSeries(Long id) {
        log.debug("Deleting series with id: {}", id);
        if (!seriesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Series", "id", id);
        }
        seriesRepository.deleteById(id);
    }
    
    public void incrementSeriesViewCount(Long id) {
        log.debug("Incrementing view count for series with id: {}", id);
        seriesRepository.incrementViewCount(id);
    }

    // Season related methods
    
    public List<SeasonDTO> getSeasonsBySeriesId(Long seriesId) {
        log.debug("Fetching seasons for series id: {}", seriesId);
        if (!seriesRepository.existsById(seriesId)) {
            throw new ResourceNotFoundException("Series", "id", seriesId);
        }
        
        return seasonRepository.findBySeriesIdOrderBySeasonNumber(seriesId).stream()
                .map(catalogMapper::seasonToSeasonDTO)
                .collect(Collectors.toList());
    }
    
    public SeasonDTO getSeasonById(Long id) {
        log.debug("Fetching season with id: {}", id);
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "id", id));
        return catalogMapper.seasonToSeasonDTO(season);
    }
    
    // Episode related methods
    
    public List<EpisodeDTO> getEpisodesBySeasonId(Long seasonId) {
        log.debug("Fetching episodes for season id: {}", seasonId);
        if (!seasonRepository.existsById(seasonId)) {
            throw new ResourceNotFoundException("Season", "id", seasonId);
        }
        
        return episodeRepository.findBySeasonIdOrderByEpisodeNumber(seasonId).stream()
                .map(catalogMapper::episodeToEpisodeDTO)
                .collect(Collectors.toList());
    }
    
    public EpisodeDTO getEpisodeById(Long id) {
        log.debug("Fetching episode with id: {}", id);
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "id", id));
        return catalogMapper.episodeToEpisodeDTO(episode);
    }
}

