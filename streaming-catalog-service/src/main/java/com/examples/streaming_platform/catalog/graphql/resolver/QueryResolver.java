package com.examples.streaming_platform.catalog.graphql.resolver;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.service.EpisodeService;
import com.examples.streaming_platform.catalog.service.MovieService;
import com.examples.streaming_platform.catalog.service.SeasonService;
import com.examples.streaming_platform.catalog.service.SeriesService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

public class QueryResolver {

    private final MovieService movieService;
    private final SeriesService seriesService;
    private final SeasonService seasonService;
    private final EpisodeService episodeService;

    public QueryResolver(MovieService movieService,
                         SeriesService seriesService,
                         SeasonService seasonService,
                         EpisodeService episodeService) {
        this.movieService = movieService;
        this.seriesService = seriesService;
        this.seasonService = seasonService;
        this.episodeService = episodeService;
    }

    // Hello query
    @QueryMapping
    public String hello() {
        return "Hello, GraphQL!";
    }

    // Movie queries
    @QueryMapping
    public Movie movie(@Argument Long id) {
        return movieService.getMovieById(id);
    }

    @QueryMapping
    public Page<Movie> movies() {
        return movieService.getAllMovies(PageRequest.of(0, 20));
    }

    @QueryMapping
    public Page<Movie> searchMovies(@Argument String title) {
        return movieService.searchMoviesByTitle(title, PageRequest.of(0, 20));
    }

    @QueryMapping
    public Page<Movie> moviesByGenre(@Argument String genre) {
        return movieService.getMoviesByGenre(genre, PageRequest.of(0, 20));
    }

    @QueryMapping
    public List<Movie> topRatedMovies() {
        return movieService.getTopRatedMovies();
    }

    @QueryMapping
    public List<Movie> featuredMovies() {
        return movieService.getFeaturedMovies();
    }

    @QueryMapping
    public Map<String, Object> moviesPage(@Argument Integer page, @Argument Integer size) {
        PageRequest pageRequest = PageRequest.of(page != null ? page : 0, size != null ? size : 20);
        Page<Movie> moviePage = movieService.getAllMovies(pageRequest);

        return Map.of(
                "content", moviePage.getContent(),
                "totalElements", moviePage.getTotalElements(),
                "totalPages", moviePage.getTotalPages()
        );
    }

    // Series queries
    @QueryMapping
    public SeriesDTO series(@Argument Long id) {
        return seriesService.getSeriesById(id);
    }

    @QueryMapping
    public Page<SeriesDTO> allSeries() {
        return seriesService.getAllSeries(PageRequest.of(0, 20));
    }

    @QueryMapping
    public Page<SeriesDTO> searchSeries(@Argument String title) {
        return seriesService.searchSeriesByTitle(title, PageRequest.of(0, 20));
    }

    @QueryMapping
    public Page<SeriesDTO> seriesByGenre(@Argument String genre) {
        return seriesService.getSeriesByGenre(genre, PageRequest.of(0, 20));
    }

    @QueryMapping
    public List<SeriesDTO> topRatedSeries() {
        return seriesService.getTopRatedSeries();
    }

    @QueryMapping
    public List<SeriesDTO> featuredSeries() {
        return seriesService.getFeaturedSeries();
    }

    @QueryMapping
    public Map<String, Object> seriesPage(@Argument Integer page, @Argument Integer size) {
        PageRequest pageRequest = PageRequest.of(page != null ? page : 0, size != null ? size : 20);
        Page<SeriesDTO> seriesPage = seriesService.getAllSeries(pageRequest);

        return Map.of(
                "content", seriesPage.getContent(),
                "totalElements", seriesPage.getTotalElements(),
                "totalPages", seriesPage.getTotalPages()
        );
    }

    // Season queries
    @QueryMapping
    public SeasonDTO season(@Argument Long id) {
        return seasonService.getSeasonById(id);
    }

    @QueryMapping
    public List<SeasonDTO> seasons(@Argument Long seriesId) {
        return seasonService.getSeasonsBySeries(seriesId);
    }

    @QueryMapping
    public SeasonDTO seasonByNumber(@Argument Long seriesId, @Argument Integer seasonNumber) {
        return seasonService.getSeasonBySeriesAndNumber(seriesId, seasonNumber);
    }

    // Episode queries
    @QueryMapping
    public EpisodeDTO episode(@Argument Long id) {
        return episodeService.getEpisodeById(id);
    }

    @QueryMapping
    public List<EpisodeDTO> episodes(@Argument Long seasonId) {
        return episodeService.getEpisodesBySeasonId(seasonId);
    }
}