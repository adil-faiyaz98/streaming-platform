package com.examples.streaming_platform.catalog.mapper;

import com.examples.streaming_platform.catalog.dto.*;
import com.examples.streaming_platform.catalog.model.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatalogMapper {

    // TVShow mappings
    TvShowDTO tvShowToTvShowDTO(TvShow tvShow);

    TvShow tvShowDTOToTvShow(TvShowDTO tvShowDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTvShowFromDTO(TvShowDTO dto, @MappingTarget TvShow tvShow);

    // Movie mappings
    MovieDTO movieToMovieDTO(Movie movie);
    
    Movie movieDTOToMovie(MovieDTO movieDTO);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMovieFromDTO(MovieDTO dto, @MappingTarget Movie movie);

    // Series mappings
    SeriesDTO seriesToSeriesDTO(Series series);
    
    Series seriesDTOToSeries(SeriesDTO seriesDTO);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSeriesFromDTO(SeriesDTO dto, @MappingTarget Series series);

    // Season mappings
    SeasonDTO seasonToSeasonDTO(Season season);
    
    Season seasonDTOToSeason(SeasonDTO seasonDTO);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSeasonFromDTO(SeasonDTO dto, @MappingTarget Season season);

    // Episode mappings
    EpisodeDTO episodeToEpisodeDTO(Episode episode);
    
    Episode episodeDTOToEpisode(EpisodeDTO episodeDTO);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEpisodeFromDTO(EpisodeDTO dto, @MappingTarget Episode episode);
}