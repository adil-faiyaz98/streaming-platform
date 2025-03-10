package com.examples.streaming_platform.catalog.mapper;

import com.examples.streaming_platform.catalog.dto.*;
import com.examples.streaming_platform.catalog.controller.SeriesController;
import com.examples.streaming_platform.catalog.model.*;
import org.mapstruct.*;

/**
 * Central MapStruct mapper for converting domain entities (Movie, SeriesController, Season, Episode, TvShow)
 * to their corresponding DTOs and vice versa.
 *
 * <p>Uses after-mapping methods to handle special logic (e.g., mapping season IDs on episodes).</p>
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatalogMapper {

    // ------------------- TVShow Mappings -------------------

    /**
     * Convert a TvShow entity to a TvShowDTO.
     */
    TvShowDTO tvShowToTvShowDTO(TvShow tvShow);

    /**
     * Convert a TvShowDTO to a TvShow entity.
     */
    TvShow tvShowDTOToTvShow(TvShowDTO tvShowDTO);

    /**
     * Update an existing TvShow entity with data from a TvShowDTO.
     * Null fields in dto are ignored.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTvShowFromDTO(TvShowDTO dto, @MappingTarget TvShow tvShow);


    // ------------------- Movie Mappings -------------------

    /**
     * Convert a Movie entity to a MovieDTO.
     */
    MovieDTO movieToMovieDTO(Movie movie);

    /**
     * Convert a MovieDTO to a Movie entity.
     */
    Movie movieDTOToMovie(MovieDTO movieDTO);

    /**
     * Update an existing Movie entity with data from a MovieDTO.
     * Null fields in dto are ignored.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMovieFromDTO(MovieDTO dto, @MappingTarget Movie movie);


    // ------------------- SeriesController Mappings -------------------

    /**
     * Convert a SeriesController entity to a SeriesDTO.
     */
    SeriesDTO seriesToSeriesDTO(Series series);

    /**
     * Convert a SeriesDTO to a SeriesController entity.
     */
    Series seriesDTOToSeries(SeriesDTO seriesDTO);

    /**
     * Update an existing SeriesController entity with data from a SeriesDTO.
     * Null fields in dto are ignored.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSeriesFromDTO(SeriesDTO dto, @MappingTarget Series series);


    // ------------------- Season Mappings -------------------

    /**
     * Convert a Season entity to a SeasonDTO.
     */
    SeasonDTO seasonToSeasonDTO(Season season);

    /**
     * Convert a SeasonDTO to a Season entity.
     */
    Season seasonDTOToSeason(SeasonDTO seasonDTO);

    /**
     * Update an existing Season entity with data from a SeasonDTO.
     * Null fields in dto are ignored.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateSeasonFromDTO(SeasonDTO dto, @MappingTarget Season season);


    // ------------------- Episode Mappings -------------------

    /**
     * Convert an Episode entity to an EpisodeDTO.
     */
    EpisodeDTO episodeToEpisodeDTO(Episode episode);

    /**
     * Convert an EpisodeDTO to an Episode entity.
     */
    Episode episodeDTOToEpisode(EpisodeDTO episodeDTO);

    /**
     * Update an existing Episode entity with data from an EpisodeDTO.
     * Null fields in dto are ignored.
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEpisodeFromDTO(EpisodeDTO dto, @MappingTarget Episode episode);


    // ------------------- Custom After-Mapping Logic -------------------

    /**
     * After mapping an Episode entity to EpisodeDTO, copy the Season ID if present.
     */
    @AfterMapping
    default void mapSeasonToSeasonId(@MappingTarget EpisodeDTO target, Episode source) {
        if (source.getSeason() != null) {
            target.setSeasonId(source.getSeason().getId());
        }
    }

    /**
     * After mapping an EpisodeDTO to an Episode entity, set the Season object if seasonId is present.
     */
    @AfterMapping
    default void mapSeasonIdToSeason(@MappingTarget Episode target, EpisodeDTO source) {
        if (source.getSeasonId() != null) {
            Season season = new Season();
            season.setId(source.getSeasonId());
            target.setSeason(season);
        }
    }
}
