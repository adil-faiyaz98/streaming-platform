package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.*;
import com.examples.streaming_platform.catalog.graphql.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.*;
import com.examples.streaming_platform.catalog.monitoring.MetricService;
import com.examples.streaming_platform.catalog.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * CatalogService handles operations related to movies, series, seasons, and episodes.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class CatalogService {

    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;
    private final CatalogMapper catalogMapper;
    private final MetricService metricService;

    // ----- Movie Methods -----

    /**
     * Get all movies with pagination.
     *
     * @param pageable pagination information
     * @return a page of movies
     */
    @Cacheable(value = "movies", key = "'page_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        log.debug("Fetching all movies with pagination: {}", pageable);
        long startTime = System.currentTimeMillis();

        try {
            Page<MovieDTO> result = movieRepository.findAll(pageable)
                    .map(catalogMapper::movieToMovieDTO);
            return result;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            metricService.recordDatabaseOperation("select", "movie", executionTime);
        }
    }

    /**
     * Get a movie by ID.
     *
     * @param id the movie ID
     * @return the movie DTO
     * @throws ResourceNotFoundException if the movie is not found
     */
    @Cacheable(value = "movies", key = "#id")
    public MovieDTO getMovieById(Long id) {
        log.debug("Fetching movie by ID: {}", id);
        long startTime = System.currentTimeMillis();

        try {
            Movie movie = movieRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
            return catalogMapper.movieToMovieDTO(movie);
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            metricService.recordDatabaseOperation("select", "movie", executionTime);
        }
    }

    public Page<MovieDTO> searchMoviesByTitle(String title, Pageable page) {
        Page<Movie> results = movieRepository.findByTitleContainingIgnoreCase(title, page);
        return results.map(catalogMapper::movieToMovieDTO);
    }


    public Page<MovieDTO> getMoviesByGenre(String genre, Pageable pageable) {
        return movieRepository.findMoviesByGenre(genre, pageable)
                .map(catalogMapper::movieToMovieDTO);
    }

    /**
     * Get top rated movies.
     *
     * @return a list of top rated movies
     */
    @Cacheable(value = "movies", key = "'top-rated'")
    public List<MovieDTO> getTopRatedMovies() {
        log.debug("Fetching top-rated movies");
        long startTime = System.currentTimeMillis();

        try {
            List<Movie> topRatedMovies = movieRepository.findTop10ByOrderByAverageRatingDesc();
            return topRatedMovies.stream()
                    .map(catalogMapper::movieToMovieDTO)
                    .toList();
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            metricService.recordDatabaseOperation("select", "movie", executionTime);
        }
    }

    /**
     * Get movies by IDs.
     *
     * @param ids the movie IDs
     * @return a list of movies
     */
    public List<MovieDTO> getMoviesByIds(List<String> ids) {
        log.debug("Fetching movies by IDs: {}", ids);
        long startTime = System.currentTimeMillis();

        try {
            List<Long> longIds = ids.stream()
                    .map(id -> {
                        try {
                            return Long.parseLong(id);
                        } catch (NumberFormatException e) {
                            log.warn("Invalid movie ID: {}", id);
                            return null;
                        }
                    })
                    .filter(id -> id != null)
                    .toList();

            List<Movie> movies = movieRepository.findAllById(longIds);
            return movies.stream()
                    .map(catalogMapper::movieToMovieDTO)
                    .toList();
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            metricService.recordDatabaseOperation("select", "movie", executionTime);
        }
    }

    public List<MovieDTO> getFeaturedMovies() {
        return movieRepository.findByFeaturedTrue().stream()
                .map(catalogMapper::movieToMovieDTO)
                .toList();
    }

    /**
     * Create a new movie.
     *
     * @param movieDTO the movie DTO
     * @return the created movie DTO
     */
    @Transactional
    @CacheEvict(value = {"movies", "featured"}, allEntries = true)
    public MovieDTO createMovie(MovieDTO movieDTO) {
        log.debug("Creating new movie: {}", movieDTO.getTitle());
        long startTime = System.currentTimeMillis();

        try {
            Movie movie = catalogMapper.movieDTOToMovie(movieDTO);
            Movie saved = movieRepository.save(movie);
            metricService.incrementCounter("movie.created");
            return catalogMapper.movieToMovieDTO(saved);
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            metricService.recordDatabaseOperation("insert", "movie", executionTime);
        }
    }

    /**
     * Update a movie.
     *
     * @param id the movie ID
     * @param movieDTO the movie DTO
     * @return the updated movie DTO
     * @throws ResourceNotFoundException if the movie is not found
     */
    @Transactional
    @CacheEvict(value = {"movies", "featured"}, key = "#id")
    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
        log.debug("Updating movie with ID: {}", id);
        long startTime = System.currentTimeMillis();

        try {
            Movie existing = movieRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
            catalogMapper.updateMovieFromDTO(movieDTO, existing);
            Movie updated = movieRepository.save(existing);
            metricService.incrementCounter("movie.updated");
            return catalogMapper.movieToMovieDTO(updated);
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            metricService.recordDatabaseOperation("update", "movie", executionTime);
        }
    }

    /**
     * Delete a movie.
     *
     * @param id the movie ID
     * @throws ResourceNotFoundException if the movie is not found
     */
    @Transactional
    @CacheEvict(value = {"movies", "featured"}, allEntries = true)
    public void deleteMovie(Long id) {
        log.debug("Deleting movie with ID: {}", id);
        long startTime = System.currentTimeMillis();

        try {
            if (!movieRepository.existsById(id)) {
                throw new ResourceNotFoundException("Movie", "id", id);
            }
            movieRepository.deleteById(id);
            metricService.incrementCounter("movie.deleted");
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            metricService.recordDatabaseOperation("delete", "movie", executionTime);
        }
    }

    public void incrementMovieViewCount(Long id) {
        movieRepository.incrementViewCount(id);
    }

    public Map<String, Long> getMovieGenreStats() {
        Map<String, Long> stats = new HashMap<>();
        movieRepository.findAll().forEach(movie -> {
            movie.getGenres().forEach(genre ->
                    stats.put(genre.toString(), stats.getOrDefault(genre.toString(), 0L) + 1));

        });
        return stats;
    }

    // ----- SeriesController Methods -----

    public Page<SeriesDTO> getAllSeries(Pageable pageable) {
        return seriesRepository.findAll(pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }

    public SeriesDTO getSeriesById(Long id) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SeriesController", "id", id));
        return catalogMapper.seriesToSeriesDTO(series);
    }

    public Page<SeriesDTO> searchSeriesByTitle(String title, Pageable pageable) {
        return seriesRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }

    public Page<SeriesDTO> getSeriesByGenre(String genre, Pageable pageable) {
        return seriesRepository.findByGenresContainingIgnoreCase(genre, pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }

    public List<SeriesDTO> getTopRatedSeries() {
        return seriesRepository.findTop10ByOrderByAverageRatingDesc().stream()
                .map(catalogMapper::seriesToSeriesDTO)
                .toList();
    }

    public List<SeriesDTO> getFeaturedSeries() {
        return seriesRepository.findByFeaturedTrue().stream()
                .map(catalogMapper::seriesToSeriesDTO)
                .toList();
    }

    public SeriesDTO createSeries(SeriesDTO seriesDTO) {
        Series series = catalogMapper.seriesDTOToSeries(seriesDTO);
        Series saved = seriesRepository.save(series);
        return catalogMapper.seriesToSeriesDTO(saved);
    }

    public SeriesDTO updateSeries(Long id, SeriesDTO seriesDTO) {
        Series existing = seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SeriesController", "id", id));
        catalogMapper.updateSeriesFromDTO(seriesDTO, existing);
        Series updated = seriesRepository.save(existing);
        return catalogMapper.seriesToSeriesDTO(updated);
    }

    public void deleteSeries(Long id) {
        if (!seriesRepository.existsById(id))
            throw new ResourceNotFoundException("SeriesController", "id", id);
        seriesRepository.deleteById(id);
    }

    public void incrementSeriesViewCount(Long id) {
        seriesRepository.incrementViewCount(id);
    }

    // ----- Season Methods -----

    public List<SeasonDTO> getSeasonsBySeriesId(Long seriesId) {
        if (!seriesRepository.existsById(seriesId))
            throw new ResourceNotFoundException("SeriesController", "id", seriesId);
        return seasonRepository.findBySeriesIdOrderBySeasonNumber(seriesId)
                .stream()
                .map(catalogMapper::seasonToSeasonDTO)
                .toList();
    }

    public SeasonDTO getSeasonById(Long id) {
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "id", id));
        return catalogMapper.seasonToSeasonDTO(season);
    }

    public SeasonDTO createSeason(Long seriesId, SeasonDTO seasonDTO) {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResourceNotFoundException("SeriesController", "id", seriesId));
        Season season = catalogMapper.seasonDTOToSeason(seasonDTO);
        season.setSeries(series);
        Season saved = seasonRepository.save(season);
        return catalogMapper.seasonToSeasonDTO(saved);
    }

    public SeasonDTO updateSeason(Long id, SeasonDTO seasonDTO) {
        Season existing = seasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "id", id));
        catalogMapper.updateSeasonFromDTO(seasonDTO, existing);
        Season updated = seasonRepository.save(existing);
        return catalogMapper.seasonToSeasonDTO(updated);
    }

    public void deleteSeason(Long id) {
        if (!seasonRepository.existsById(id))
            throw new ResourceNotFoundException("Season", "id", id);
        seasonRepository.deleteById(id);
    }

    // ----- Episode Methods -----

    public List<EpisodeDTO> getEpisodesBySeasonId(Long seasonId) {
        if (!seasonRepository.existsById(seasonId))
            throw new ResourceNotFoundException("Season", "id", seasonId);
        return episodeRepository.findBySeasonIdOrderByEpisodeNumber(seasonId)
                .stream().map(catalogMapper::episodeToEpisodeDTO).toList();
    }

    public List<EpisodeDTO> getEpisodesBySeriesId(Long seriesId) {
        if (!seriesRepository.existsById(seriesId))
            throw new ResourceNotFoundException("SeriesController", "id", seriesId);

        return seasonRepository.findBySeriesIdOrderBySeasonNumber(seriesId).stream()
                .flatMap(season -> episodeRepository.findBySeasonIdOrderByEpisodeNumber(season.getId()).stream())
                .map(catalogMapper::episodeToEpisodeDTO)
                .toList();
    }

    public EpisodeDTO getEpisodeById(Long id) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "id", id));
        return catalogMapper.episodeToEpisodeDTO(episode);
    }

    public EpisodeDTO createEpisode(Long seasonId, EpisodeDTO episodeDTO) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "id", seasonId));
        Episode episode = catalogMapper.episodeDTOToEpisode(episodeDTO);
        episode.setSeason(season);
        Episode saved = episodeRepository.save(episode);
        return catalogMapper.episodeToEpisodeDTO(saved);
    }

    public EpisodeDTO updateEpisode(Long id, EpisodeDTO episodeDTO) {
        Episode existing = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "id", id));
        catalogMapper.updateEpisodeFromDTO(episodeDTO, existing);
        Episode updated = episodeRepository.save(existing);
        return catalogMapper.episodeToEpisodeDTO(updated);
    }

    public void deleteEpisode(Long id) {
        if (!episodeRepository.existsById(id))
            throw new ResourceNotFoundException("Episode", "id", id);
        episodeRepository.deleteById(id);
    }
}
