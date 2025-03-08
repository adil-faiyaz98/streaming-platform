package main.java.com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.*;
import com.examples.streaming_platform.catalog.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.*;
import com.examples.streaming_platform.catalog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {
    
    private final MovieRepository movieRepository;
    private final SeriesRepository seriesRepository;
    private final SeasonRepository seasonRepository;
    private final EpisodeRepository episodeRepository;
    private final CatalogMapper catalogMapper;

    // Movie operations
    @Override
    @Cacheable(value = "movies", key = "'all-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<MovieDTO> getAllMovies(Pageable pageable) {
        return movieRepository.findAll(pageable)
                .map(catalogMapper::movieToMovieDTO);
    }

    @Override
    @Cacheable(value = "movies", key = "#id")
    public MovieDTO getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(catalogMapper::movieToMovieDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
    }

    @Override
    public Page<MovieDTO> searchMoviesByTitle(String title, Pageable pageable) {
        return movieRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(catalogMapper::movieToMovieDTO);
    }

    @Override
    @Cacheable(value = "moviesByGenre", key = "#genre + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<MovieDTO> getMoviesByGenre(String genre, Pageable pageable) {
        return movieRepository.findByGenre(genre, pageable)
                .map(catalogMapper::movieToMovieDTO);
    }

    @Override
    @Cacheable(value = "topRatedMovies")
    public List<MovieDTO> getTopRatedMovies() {
        return movieRepository.findTop10ByOrderByAverageRatingDesc()
                .stream()
                .map(catalogMapper::movieToMovieDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDTO> getFeaturedMovies() {
        return movieRepository.findByFeaturedTrue()
                .stream()
                .map(catalogMapper::movieToMovieDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = {"movies", "moviesByGenre", "topRatedMovies"}, allEntries = true)
    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = catalogMapper.movieDTOToMovie(movieDTO);
        movie.setCreatedAt(ZonedDateTime.now());
        movie.setUpdatedAt(ZonedDateTime.now());
        Movie savedMovie = movieRepository.save(movie);
        return catalogMapper.movieToMovieDTO(savedMovie);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"movies", "moviesByGenre", "topRatedMovies"}, allEntries = true)
    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
        Movie movie = movieRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movie", "id", id));
        
        catalogMapper.updateMovieFromDTO(movieDTO, movie);
        movie.setUpdatedAt(ZonedDateTime.now());
        Movie updatedMovie = movieRepository.save(movie);
        return catalogMapper.movieToMovieDTO(updatedMovie);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"movies", "moviesByGenre", "topRatedMovies"}, allEntries = true)
    public void deleteMovie(Long id) {
        if (!movieRepository.existsById(id)) {
            throw new ResourceNotFoundException("Movie", "id", id);
        }
        movieRepository.deleteById(id);
    }
    
    @Override
    public Map<String, Long> getMovieGenreStats() {
        // Implementation depends on specific repository method or query
        Map<String, Long> result = new HashMap<>();
        // Populate the map with genre name -> count
        return result;
    }

    // Series operations
    @Override
    @Cacheable(value = "series", key = "'all-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<SeriesDTO> getAllSeries(Pageable pageable) {
        return seriesRepository.findAll(pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }

    @Override
    @Cacheable(value = "series", key = "#id")
    public SeriesDTO getSeriesById(Long id) {
        return seriesRepository.findById(id)
                .map(catalogMapper::seriesToSeriesDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Series", "id", id));
    }

    @Override
    public Page<SeriesDTO> searchSeriesByTitle(String title, Pageable pageable) {
        return seriesRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }

    @Override
    @Cacheable(value = "seriesByGenre", key = "#genre + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<SeriesDTO> getSeriesByGenre(String genre, Pageable pageable) {
        return seriesRepository.findByGenre(genre, pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }

    @Override
    @Cacheable(value = "topRatedSeries")
    public List<SeriesDTO> getTopRatedSeries() {
        return seriesRepository.findTop10ByOrderByAverageRatingDesc()
                .stream()
                .map(catalogMapper::seriesToSeriesDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeriesDTO> getFeaturedSeries() {
        return seriesRepository.findAllFeatured()
                .stream()
                .map(catalogMapper::seriesToSeriesDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    @CacheEvict(value = {"series", "seriesByGenre", "topRatedSeries"}, allEntries = true)
    public SeriesDTO createSeries(SeriesDTO seriesDTO) {
        Series series = catalogMapper.seriesDTOToSeries(seriesDTO);
        series.setCreatedAt(ZonedDateTime.now());
        series.setUpdatedAt(ZonedDateTime.now());
        Series savedSeries = seriesRepository.save(series);
        return catalogMapper.seriesToSeriesDTO(savedSeries);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"series", "seriesByGenre", "topRatedSeries"}, allEntries = true)
    public SeriesDTO updateSeries(Long id, SeriesDTO seriesDTO) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Series", "id", id));
        
        catalogMapper.updateSeriesFromDTO(seriesDTO, series);
        series.setUpdatedAt(ZonedDateTime.now());
        Series updatedSeries = seriesRepository.save(series);
        return catalogMapper.seriesToSeriesDTO(updatedSeries);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"series", "seriesByGenre", "topRatedSeries"}, allEntries = true)
    public void deleteSeries(Long id) {
        if (!seriesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Series", "id", id);
        }
        seriesRepository.deleteById(id);
    }
    
    @Override
    public Map<String, Long> getSeriesGenreStats() {
        // Implementation depends on specific repository method or query
        Map<String, Long> result = new HashMap<>();
        // Populate the map with genre name -> count
        return result;
    }

    // Season operations
    @Override
    @Cacheable(value = "seasons", key = "'series-' + #seriesId")
    public List<SeasonDTO> getSeasonsBySeriesId(Long seriesId) {
        if (!seriesRepository.existsById(seriesId)) {
            throw new ResourceNotFoundException("Series", "id", seriesId);
        }
        return seasonRepository.findBySeriesIdOrderBySeasonNumber(seriesId).stream()
                .map(catalogMapper::seasonToSeasonDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "seasons", key = "#id")
    public SeasonDTO getSeasonById(Long id) {
        return seasonRepository.findById(id)
                .map(catalogMapper::seasonToSeasonDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "id", id));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"seasons", "series"}, allEntries = true)
    public SeasonDTO createSeason(Long seriesId, SeasonDTO seasonDTO) {
        Series series = seriesRepository.findById(seriesId)
                .orElseThrow(() -> new ResourceNotFoundException("Series", "id", seriesId));
        
        Season season = catalogMapper.seasonDTOToSeason(seasonDTO);
        season.setSeries(series);
        season.setCreatedAt(ZonedDateTime.now());
        season.setUpdatedAt(ZonedDateTime.now());
        Season savedSeason = seasonRepository.save(season);
        return catalogMapper.seasonToSeasonDTO(savedSeason);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"seasons", "series"}, allEntries = true)
    public SeasonDTO updateSeason(Long id, SeasonDTO seasonDTO) {
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "id", id));
        
        catalogMapper.updateSeasonFromDTO(seasonDTO, season);
        season.setUpdatedAt(ZonedDateTime.now());
        Season updatedSeason = seasonRepository.save(season);
        return catalogMapper.seasonToSeasonDTO(updatedSeason);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"seasons", "series"}, allEntries = true)
    public void deleteSeason(Long id) {
        if (!seasonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Season", "id", id);
        }
        seasonRepository.deleteById(id);
    }

    // Episode operations
    @Override
    @Cacheable(value = "episodes", key = "'season-' + #seasonId")
    public List<EpisodeDTO> getEpisodesBySeasonId(Long seasonId) {
        if (!seasonRepository.existsById(seasonId)) {
            throw new ResourceNotFoundException("Season", "id", seasonId);
        }
        return episodeRepository.findBySeasonIdOrderByEpisodeNumber(seasonId).stream()
                .map(catalogMapper::episodeToEpisodeDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "episodes", key = "#id")
    public EpisodeDTO getEpisodeById(Long id) {
        return episodeRepository.findById(id)
                .map(catalogMapper::episodeToEpisodeDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "id", id));
    }

    @Override
    @Transactional
    @CacheEvict(value = {"episodes", "seasons"}, allEntries = true)
    public EpisodeDTO createEpisode(Long seasonId, EpisodeDTO episodeDTO) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "id", seasonId));
        
        Episode episode = catalogMapper.episodeDTOToEpisode(episodeDTO);
        episode.setSeason(season);
        episode.setCreatedAt(ZonedDateTime.now());
        episode.setUpdatedAt(ZonedDateTime.now());
        Episode savedEpisode = episodeRepository.save(episode);
        return catalogMapper.episodeToEpisodeDTO(savedEpisode);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"episodes", "seasons"}, allEntries = true)
    public EpisodeDTO updateEpisode(Long id, EpisodeDTO episodeDTO) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "id", id));
        
        catalogMapper.updateEpisodeFromDTO(episodeDTO, episode);
        episode.setUpdatedAt(ZonedDateTime.now());
        Episode updatedEpisode = episodeRepository.save(episode);
        return catalogMapper.episodeToEpisodeDTO(updatedEpisode);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"episodes", "seasons"}, allEntries = true)
    public void deleteEpisode(Long id) {
        if (!episodeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Episode", "id", id);
        }
        episodeRepository.deleteById(id);
    }
}