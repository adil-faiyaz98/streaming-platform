package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.controller.SeriesController;
import com.examples.streaming_platform.catalog.dto.*;
import com.examples.streaming_platform.catalog.graphql.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.*;
import com.examples.streaming_platform.catalog.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * CatalogService handles operations related to movies, series, seasons, and episodes.
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

    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        log.debug("Fetching all movies with pagination: {}", pageable);
        return movieRepository.findAll(pageable)
                .map(catalogMapper::movieToMovieDTO);
    }

    public MovieDTO getMovieById(Long id) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        return catalogMapper.movieToMovieDTO(movie);
    }

    public Page<MovieDTO> searchMoviesByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(catalogMapper::movieToMovieDTO);
    }

    public Page<MovieDTO> getMoviesByGenre(String genre, Pageable pageable) {
        return movieRepository.findByGenre(genre, pageable)
                .map(catalogMapper::movieToMovieDTO);
    }

    public List<MovieDTO> getTopRatedMovies() {
        return movieRepository.findTop10ByOrderByAverageRatingDesc().stream()
                .map(catalogMapper::movieToMovieDTO)
                .toList();
    }

    public List<MovieDTO> getFeaturedMovies() {
        return movieRepository.findByFeaturedTrue().stream()
                .map(catalogMapper::movieToMovieDTO)
                .toList();
    }

    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = catalogMapper.movieDTOToMovie(movieDTO);
        Movie saved = movieRepository.save(movie);
        return catalogMapper.movieToMovieDTO(saved);
    }

    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
        Movie existing = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        catalogMapper.updateMovieFromDTO(movieDTO, existing);
        Movie updated = movieRepository.save(existing);
        return catalogMapper.movieToMovieDTO(updated);
    }

    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie", "id", id);
        }
        movieRepository.deleteById(id);
    }

    public void incrementMovieViewCount(Long id) {
        movieRepository.incrementViewCount(id);
    }

    public Map<String, Long> getMovieGenreStats() {
        Map<String, Long> stats = new HashMap<>();
        movieRepository.findAll().forEach(movie -> {
            movie.getGenres().forEach(genre ->
                    stats.put(genre, stats.getOrDefault(genre, 0L) + 1));
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
        return seriesRepository.findByGenre(genre, pageable)
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
