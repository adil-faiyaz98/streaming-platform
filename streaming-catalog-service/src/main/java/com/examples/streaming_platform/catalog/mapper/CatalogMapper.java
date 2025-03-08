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
    @Mapping(source = "seasonId", target = "seasonId")
    EpisodeDTO episodeToEpisodeDTO(Episode episode);
    
    @Mapping(source = "seasonId", target = "seasonId")
    Episode episodeDTOToEpisode(EpisodeDTO episodeDTO);
    
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "seasonId", target = "seasonId")
    void updateEpisodeFromDTO(EpisodeDTO dto, @MappingTarget Episode episode);
    
    // Helper methods for Season <-> Long conversion
    default Long mapSeasonToId(Season season) {
        return season != null ? season.getSeasonId() : null;
    }
    
    default Season mapIdToSeason(Long id) {
        if (id == null) {
            return null;
        }
        Season season = new Season();
        season.setSeasonId(id);
        return season;
    }
}