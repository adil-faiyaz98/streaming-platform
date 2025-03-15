package com.examples.streaming_platform.catalog.mapper;

import com.examples.streaming_platform.catalog.dto.*;
import com.examples.streaming_platform.catalog.model.*;
import org.mapstruct.*;

/**
 * Central MapStruct mapper for converting domain entities (Movie, Series, Season, Episode, TvShow)
 * to their corresponding DTOs and vice versa.
 *
 * <p>Uses after-mapping methods to handle special logic (e.g., mapping season IDs on episodes).</p>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatalogMapper {

    // ------------------- TVShow Mappings -------------------
    TvShowDTO tvShowToTvShowDTO(TvShow tvShow);
    TvShow tvShowDTOToTvShow(TvShowDTO tvShowDTO);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTvShowFromDTO(TvShowDTO dto, @MappingTarget TvShow tvShow);

    // ------------------- Movie Mappings -------------------
    MovieDTO movieToMovieDTO(Movie movie);
    Movie movieDTOToMovie(MovieDTO movieDTO);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMovieFromDTO(MovieDTO dto, @MappingTarget Movie movie);

    // **NEW: Mapping MovieInput to Movie**
    @Mapping(target = "id", ignore = true) // Ignore ID as it's usually generated
    Movie movieInputToMovie(MovieInput input);

    // ------------------- Series Mappings -------------------
    SeriesDTO seriesToSeriesDTO(Series series);
    Series seriesDTOToSeries(SeriesDTO seriesDTO);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSeriesFromDTO(SeriesDTO dto, @MappingTarget Series series);

    // **NEW: Mapping SeriesInput to SeriesDTO**
    @Mapping(target = "id", ignore = true)
    SeriesDTO seriesInputToSeriesDTO(SeriesInput input);

    // ------------------- Season Mappings -------------------
    SeasonDTO seasonToSeasonDTO(Season season);
    Season seasonDTOToSeason(SeasonDTO seasonDTO);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSeasonFromDTO(SeasonDTO dto, @MappingTarget Season season);

    // **NEW: Mapping SeasonInput to SeasonDTO**
    @Mapping(target = "id", ignore = true)
    SeasonDTO seasonInputToSeasonDTO(SeasonInput input);

    // ------------------- Episode Mappings -------------------
    EpisodeDTO episodeToEpisodeDTO(Episode episode);
    Episode episodeDTOToEpisode(EpisodeDTO episodeDTO);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEpisodeFromDTO(EpisodeDTO dto, @MappingTarget Episode episode);

    // **NEW: Mapping EpisodeInput to EpisodeDTO**
    @Mapping(target = "id", ignore = true)
    EpisodeDTO episodeInputToEpisodeDTO(EpisodeInput input);

    // ------------------- Custom After-Mapping Logic -------------------
    @AfterMapping
    default void mapSeasonToSeasonId(@MappingTarget EpisodeDTO target, Episode source) {
        if (source.getSeason() != null) {
            target.setSeasonId(source.getSeason().getId());
        }
    }

    @AfterMapping
    default void mapSeasonIdToSeason(@MappingTarget Episode target, EpisodeDTO source) {
        if (source.getSeasonId() != null) {
            Season season = new Season();
            season.setId(source.getSeasonId());
            target.setSeason(season);
        }
    }
}
