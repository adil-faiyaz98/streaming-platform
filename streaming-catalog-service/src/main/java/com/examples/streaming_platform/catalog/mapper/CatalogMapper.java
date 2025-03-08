// mapper/CatalogMapper.java
package com.examples.streaming_platform.catalog.mapper;

import com.examples.streaming_platform.catalog.dto.*;
import com.examples.streaming_platform.catalog.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CatalogMapper {
    // Movie mappings
    MovieDTO movieToMovieDTO(Movie movie);
    Movie movieDTOToMovie(MovieDTO movieDTO);
    void updateMovieFromDTO(MovieDTO movieDTO, @MappingTarget Movie movie);
    
    // Series mappings
    @Mapping(target = "seasons", ignore = true)
    SeriesDTO seriesToSeriesDTO(Series series);
    
    Series seriesDTOToSeries(SeriesDTO seriesDTO);
    
    @Mapping(target = "seasons", ignore = true)
    void updateSeriesFromDTO(SeriesDTO seriesDTO, @MappingTarget Series series);
    
    // Season mappings
    @Mapping(target = "series", ignore = true)
    @Mapping(target = "episodes", ignore = true)
    SeasonDTO seasonToSeasonDTO(Season season);
    
    @Mapping(target = "series", ignore = true)
    @Mapping(target = "episodes", ignore = true)
    Season seasonDTOToSeason(SeasonDTO seasonDTO);
    
    @Mapping(target = "series", ignore = true)
    void updateSeasonFromDTO(SeasonDTO seasonDTO, @MappingTarget Season season);
    
    // Episode mappings
    @Mapping(target = "season", ignore = true)
    EpisodeDTO episodeToEpisodeDTO(Episode episode);
    
    @Mapping(target = "season", ignore = true)
    Episode episodeDTOToEpisode(EpisodeDTO episodeDTO);
    
    @Mapping(target = "season", ignore = true)
    void updateEpisodeFromDTO(EpisodeDTO episodeDTO, @MappingTarget Episode episode);
}