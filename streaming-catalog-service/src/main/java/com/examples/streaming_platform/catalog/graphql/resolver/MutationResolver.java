package com.examples.streaming_platform.catalog.graphql.resolver;

import com.examples.streaming_platform.catalog.dto.*;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.*;
import com.examples.streaming_platform.catalog.service.*;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

public class MutationResolver {

    private final MovieService movieService;
    private final SeriesService seriesService;
    private final SeasonService seasonService;
    private final EpisodeService episodeService;
    private final CatalogMapper catalogMapper;

    public MutationResolver(MovieService movieService,
                            SeriesService seriesService,
                            SeasonService seasonService,
                            EpisodeService episodeService,
                            CatalogMapper catalogMapper) {
        this.movieService = movieService;
        this.seriesService = seriesService;
        this.seasonService = seasonService;
        this.episodeService = episodeService;
        this.catalogMapper = catalogMapper;
    }

    // Movie mutations
    @MutationMapping
    public Movie createMovie(@Argument MovieInput input) {
        Movie movie = catalogMapper.movieInputToMovie(input);
        return movieService.createMovie(movie);
    }

    @MutationMapping
    public Movie updateMovie(@Argument Long id, @Argument MovieInput input) {
        Movie movie = catalogMapper.movieInputToMovie(input);
        return movieService.updateMovie(id, movie);
    }

    @MutationMapping
    public Boolean deleteMovie(@Argument Long id) {
        movieService.deleteMovie(id);
        return true;
    }

    // Series mutations
    @MutationMapping("createSeriesWithInput")
    public SeriesDTO createSeries(@Argument SeriesInput input) {
        SeriesDTO series = catalogMapper.seriesInputToSeriesDTO(input);
        return seriesService.createSeries(series);
    }

    @MutationMapping
    public SeriesDTO updateSeries(@Argument Long id, @Argument SeriesInput input) {
        SeriesDTO series = catalogMapper.seriesInputToSeriesDTO(input);
        return seriesService.updateSeries(id, series);
    }

    @MutationMapping
    public Boolean deleteSeries(@Argument Long id) {
        seriesService.deleteSeries(id);
        return true;
    }

    // Season mutations
    @MutationMapping
    public SeasonDTO createSeason(@Argument Long seriesId, @Argument SeasonInput input) {
        SeasonDTO season = catalogMapper.seasonInputToSeasonDTO(input);
        return seasonService.createSeason(seriesId, season);
    }

    @MutationMapping
    public SeasonDTO updateSeason(@Argument Long id, @Argument SeasonInput input) {
        SeasonDTO season = catalogMapper.seasonInputToSeasonDTO(input);
        return seasonService.updateSeason(id, season);
    }

    @MutationMapping
    public Boolean deleteSeason(@Argument Long id) {
        seasonService.deleteSeason(id);
        return true;
    }

    // Episode mutations
    @MutationMapping
    public EpisodeDTO createEpisode(@Argument Long seasonId, @Argument EpisodeInput input) {
        EpisodeDTO episode = catalogMapper.episodeInputToEpisodeDTO(input);
        return episodeService.createEpisode(seasonId, episode);
    }

    @MutationMapping
    public EpisodeDTO updateEpisode(@Argument Long id, @Argument EpisodeInput input) {
        EpisodeDTO episode = catalogMapper.episodeInputToEpisodeDTO(input);
        return episodeService.updateEpisode(id, episode);
    }

    @MutationMapping
    public Boolean deleteEpisode(@Argument Long id) {
        episodeService.deleteEpisode(id);
        return true;
    }
}