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
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    // ----- Movie Methods -----

    @Cacheable(value = "movies", key = "'page_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        log.debug("Fetching all movies with pagination: {}", pageable);
        return movieRepository.findAll(pageable)
                .map((java.util.function.Function<? super Movie, ? extends MovieDTO>) catalogMapper::movieToMovieDTO);
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
                .map((java.util.function.Function<? super Movie, ? extends MovieDTO>) catalogMapper::movieToMovieDTO);
    }

    public Page<MovieDTO> getMoviesByGenre(String genre, Pageable pageable) {
        log.debug("Fetching movies with genre: {}", genre);
        return movieRepository.findByGenre(genre, pageable)
                .map((java.util.function.Function<? super Movie, ? extends MovieDTO>) catalogMapper::movieToMovieDTO);
    }

    @Cacheable(value = "topRatedMovies")
    public List<MovieDTO> getTopRatedMovies() {
        log.debug("Fetching top rated movies");
        return movieRepository.findTop10ByOrderByAverageRatingDesc().stream()
                .map(movie -> catalogMapper.movieToMovieDTO(movie))
                .collect(Collectors.toList());
    }

    public @NotNull List<Object> getFeaturedMovies() {
        log.debug("Fetching featured movies");
        return movieRepository.findByFeaturedTrue().stream()
                .map(object -> (Movie) object)
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

    // Implementation of previously abstract method
    public Map<String, Long> getMovieGenreStats() {
        log.debug("Calculating movie genre statistics");
        Map<String, Long> genreCounts = new HashMap<>();

        List<Movie> movies = movieRepository.findAll();
        for (Movie movie : movies) {
            for (String genre : movie.getGenres()) {
                genreCounts.put(genre, genreCounts.getOrDefault(genre, 0L) + 1);
            }
        }

        return genreCounts;
    }

    // ----- Series Methods -----

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

    // Implementation for TV Show delete (as it was abstract before)
    @Transactional
    @CacheEvict(value = {"tvShows", "topRatedTvShows"}, allEntries = true)
    public void deleteTvShow(Long id) {
        log.debug("Deleting TV show with id: {}", id);
        // TV Shows are considered the same as Series in this implementation
        deleteSeries(id);
    }

    // ----- Season Methods -----

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

    @Transactional
    @CacheEvict(value = {"seasons", "series"}, allEntries = true)
    public SeasonDTO createSeason(Long seriesId, SeasonDTO seasonDTO) {
        log.debug("Creating new season for series id: {}", seriesId);
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResourceNotFoundException("Series", "id", seriesId));

        Season season = catalogMapper.seasonDTOToSeason(seasonDTO);
        season.setSeries(series);
        Season savedSeason = seasonRepository.save(season);
        return catalogMapper.seasonToSeasonDTO(savedSeason);
    }

    @Transactional
    @CacheEvict(value = {"seasons", "series"}, allEntries = true)
    public SeasonDTO updateSeason(Long id, SeasonDTO seasonDTO) {
        log.debug("Updating season with id: {}", id);
        Season existingSeason = seasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "id", id));

        catalogMapper.updateSeasonFromDTO(seasonDTO, existingSeason);
        Season updatedSeason = seasonRepository.save(existingSeason);
        return catalogMapper.seasonToSeasonDTO(updatedSeason);
    }

    @Transactional
    @CacheEvict(value = {"seasons", "series"}, allEntries = true)
    public void deleteSeason(Long id) {
        log.debug("Deleting season with id: {}", id);
        if (!seasonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Season", "id", id);
        }
        seasonRepository.deleteById(id);
    }

    // ----- Episode Methods -----

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

    @Transactional
    @CacheEvict(value = {"episodes", "seasons"}, allEntries = true)
    public EpisodeDTO createEpisode(Long seasonId, EpisodeDTO episodeDTO) {
        log.debug("Creating new episode for season id: {}", seasonId);
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "id", seasonId));

        Episode episode = catalogMapper.episodeDTOToEpisode(episodeDTO);
        episode.setSeason(season);
        Episode savedEpisode = episodeRepository.save(episode);
        return catalogMapper.episodeToEpisodeDTO(savedEpisode);
    }

    @Transactional
    @CacheEvict(value = {"episodes", "seasons"}, allEntries = true)
    public EpisodeDTO updateEpisode(Long id, EpisodeDTO episodeDTO) {
        log.debug("Updating episode with id: {}", id);
        Episode existingEpisode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "id", id));

        catalogMapper.updateEpisodeFromDTO(episodeDTO, existingEpisode);
        Episode updatedEpisode = episodeRepository.save(existingEpisode);
        return catalogMapper.episodeToEpisodeDTO(updatedEpisode);
    }

    @Transactional
    @CacheEvict(value = {"episodes", "seasons"}, allEntries = true)
    public void deleteEpisode(Long id) {
        log.debug("Deleting episode with id: {}", id);
        if (!episodeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Episode", "id", id);
        }
        episodeRepository.deleteById(id);
    }
}

