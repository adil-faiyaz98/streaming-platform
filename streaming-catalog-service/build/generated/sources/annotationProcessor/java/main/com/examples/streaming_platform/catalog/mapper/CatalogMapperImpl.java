package com.examples.streaming_platform.catalog.mapper;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.dto.TvShowDTO;
import com.examples.streaming_platform.catalog.model.Episode;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.model.Season;
import com.examples.streaming_platform.catalog.model.Series;
import com.examples.streaming_platform.catalog.model.TvShow;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-03-08T21:13:52-0500",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.13.jar, environment: Java 21.0.5 (Oracle Corporation)"
)
@Component
public class CatalogMapperImpl implements CatalogMapper {

    @Override
    public TvShowDTO tvShowToTvShowDTO(TvShow tvShow) {
        if ( tvShow == null ) {
            return null;
        }

        TvShowDTO tvShowDTO = new TvShowDTO();

        tvShowDTO.setId( tvShow.getId() );
        tvShowDTO.setTitle( tvShow.getTitle() );
        tvShowDTO.setDescription( tvShow.getDescription() );
        tvShowDTO.setFirstAirDate( tvShow.getFirstAirDate() );
        tvShowDTO.setPosterUrl( tvShow.getPosterUrl() );
        Set<String> set = tvShow.getGenres();
        if ( set != null ) {
            tvShowDTO.setGenres( new LinkedHashSet<String>( set ) );
        }
        tvShowDTO.setRating( tvShow.getRating() );

        return tvShowDTO;
    }

    @Override
    public TvShow tvShowDTOToTvShow(TvShowDTO tvShowDTO) {
        if ( tvShowDTO == null ) {
            return null;
        }

        TvShow tvShow = new TvShow();

        tvShow.setId( tvShowDTO.getId() );
        tvShow.setTitle( tvShowDTO.getTitle() );
        tvShow.setDescription( tvShowDTO.getDescription() );
        tvShow.setFirstAirDate( tvShowDTO.getFirstAirDate() );
        tvShow.setPosterUrl( tvShowDTO.getPosterUrl() );
        Set<String> set = tvShowDTO.getGenres();
        if ( set != null ) {
            tvShow.setGenres( new LinkedHashSet<String>( set ) );
        }
        tvShow.setRating( tvShowDTO.getRating() );

        return tvShow;
    }

    @Override
    public void updateTvShowFromDTO(TvShowDTO dto, TvShow tvShow) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            tvShow.setId( dto.getId() );
        }
        if ( dto.getTitle() != null ) {
            tvShow.setTitle( dto.getTitle() );
        }
        if ( dto.getDescription() != null ) {
            tvShow.setDescription( dto.getDescription() );
        }
        if ( dto.getFirstAirDate() != null ) {
            tvShow.setFirstAirDate( dto.getFirstAirDate() );
        }
        if ( dto.getPosterUrl() != null ) {
            tvShow.setPosterUrl( dto.getPosterUrl() );
        }
        if ( tvShow.getGenres() != null ) {
            Set<String> set = dto.getGenres();
            if ( set != null ) {
                tvShow.getGenres().clear();
                tvShow.getGenres().addAll( set );
            }
        }
        else {
            Set<String> set = dto.getGenres();
            if ( set != null ) {
                tvShow.setGenres( new LinkedHashSet<String>( set ) );
            }
        }
        if ( dto.getRating() != null ) {
            tvShow.setRating( dto.getRating() );
        }
    }

    @Override
    public MovieDTO movieToMovieDTO(Movie movie) {
        if ( movie == null ) {
            return null;
        }

        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setId( movie.getId() );
        movieDTO.setTitle( movie.getTitle() );
        movieDTO.setReleaseDate( movie.getReleaseDate() );
        movieDTO.setRating( movie.getRating() );
        movieDTO.setDescription( movie.getDescription() );
        movieDTO.setReleaseYear( movie.getReleaseYear() );
        movieDTO.setDirector( movie.getDirector() );
        movieDTO.setDuration( movie.getDuration() );
        movieDTO.setMaturityRating( movie.getMaturityRating() );
        movieDTO.setImageUrl( movie.getImageUrl() );
        movieDTO.setVideoUrl( movie.getVideoUrl() );
        movieDTO.setPosterUrl( movie.getPosterUrl() );
        movieDTO.setAverageRating( movie.getAverageRating() );
        movieDTO.setViewCount( movie.getViewCount() );
        movieDTO.setFeatured( movie.getFeatured() );
        Set<String> set = movie.getGenres();
        if ( set != null ) {
            movieDTO.setGenres( new LinkedHashSet<String>( set ) );
        }
        movieDTO.setCreatedAt( movie.getCreatedAt() );
        movieDTO.setUpdatedAt( movie.getUpdatedAt() );

        return movieDTO;
    }

    @Override
    public Movie movieDTOToMovie(MovieDTO movieDTO) {
        if ( movieDTO == null ) {
            return null;
        }

        Movie movie = new Movie();

        movie.setId( movieDTO.getId() );
        movie.setTitle( movieDTO.getTitle() );
        movie.setDescription( movieDTO.getDescription() );
        movie.setReleaseYear( movieDTO.getReleaseYear() );
        movie.setReleaseDate( movieDTO.getReleaseDate() );
        movie.setDirector( movieDTO.getDirector() );
        movie.setDuration( movieDTO.getDuration() );
        movie.setMaturityRating( movieDTO.getMaturityRating() );
        movie.setImageUrl( movieDTO.getImageUrl() );
        movie.setVideoUrl( movieDTO.getVideoUrl() );
        movie.setPosterUrl( movieDTO.getPosterUrl() );
        movie.setAverageRating( movieDTO.getAverageRating() );
        movie.setViewCount( movieDTO.getViewCount() );
        movie.setFeatured( movieDTO.getFeatured() );
        if ( movieDTO.getRating() != null ) {
            movie.setRating( movieDTO.getRating() );
        }
        movie.setCreatedAt( movieDTO.getCreatedAt() );
        movie.setUpdatedAt( movieDTO.getUpdatedAt() );
        Set<String> set = movieDTO.getGenres();
        if ( set != null ) {
            movie.setGenres( new LinkedHashSet<String>( set ) );
        }

        return movie;
    }

    @Override
    public void updateMovieFromDTO(MovieDTO dto, Movie movie) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            movie.setId( dto.getId() );
        }
        if ( dto.getTitle() != null ) {
            movie.setTitle( dto.getTitle() );
        }
        if ( dto.getDescription() != null ) {
            movie.setDescription( dto.getDescription() );
        }
        if ( dto.getReleaseYear() != null ) {
            movie.setReleaseYear( dto.getReleaseYear() );
        }
        if ( dto.getReleaseDate() != null ) {
            movie.setReleaseDate( dto.getReleaseDate() );
        }
        if ( dto.getDirector() != null ) {
            movie.setDirector( dto.getDirector() );
        }
        if ( dto.getDuration() != null ) {
            movie.setDuration( dto.getDuration() );
        }
        if ( dto.getMaturityRating() != null ) {
            movie.setMaturityRating( dto.getMaturityRating() );
        }
        if ( dto.getImageUrl() != null ) {
            movie.setImageUrl( dto.getImageUrl() );
        }
        if ( dto.getVideoUrl() != null ) {
            movie.setVideoUrl( dto.getVideoUrl() );
        }
        if ( dto.getPosterUrl() != null ) {
            movie.setPosterUrl( dto.getPosterUrl() );
        }
        if ( dto.getAverageRating() != null ) {
            movie.setAverageRating( dto.getAverageRating() );
        }
        if ( dto.getViewCount() != null ) {
            movie.setViewCount( dto.getViewCount() );
        }
        if ( dto.getFeatured() != null ) {
            movie.setFeatured( dto.getFeatured() );
        }
        if ( dto.getRating() != null ) {
            movie.setRating( dto.getRating() );
        }
        if ( dto.getCreatedAt() != null ) {
            movie.setCreatedAt( dto.getCreatedAt() );
        }
        if ( dto.getUpdatedAt() != null ) {
            movie.setUpdatedAt( dto.getUpdatedAt() );
        }
        if ( movie.getGenres() != null ) {
            Set<String> set = dto.getGenres();
            if ( set != null ) {
                movie.getGenres().clear();
                movie.getGenres().addAll( set );
            }
        }
        else {
            Set<String> set = dto.getGenres();
            if ( set != null ) {
                movie.setGenres( new LinkedHashSet<String>( set ) );
            }
        }
    }

    @Override
    public SeriesDTO seriesToSeriesDTO(Series series) {
        if ( series == null ) {
            return null;
        }

        SeriesDTO seriesDTO = new SeriesDTO();

        seriesDTO.setId( series.getId() );
        seriesDTO.setTitle( series.getTitle() );
        seriesDTO.setDescription( series.getDescription() );
        seriesDTO.setStartYear( series.getStartYear() );
        seriesDTO.setEndYear( series.getEndYear() );
        Set<String> set = series.getGenres();
        if ( set != null ) {
            seriesDTO.setGenres( new LinkedHashSet<String>( set ) );
        }
        seriesDTO.setMaturityRating( series.getMaturityRating() );
        seriesDTO.setImageUrl( series.getImageUrl() );
        seriesDTO.setAverageRating( series.getAverageRating() );
        seriesDTO.setViewCount( series.getViewCount() );
        seriesDTO.setFeatured( series.getFeatured() );
        seriesDTO.setSeasons( seasonListToSeasonDTOList( series.getSeasons() ) );
        seriesDTO.setCreatedAt( series.getCreatedAt() );
        seriesDTO.setUpdatedAt( series.getUpdatedAt() );

        return seriesDTO;
    }

    @Override
    public Series seriesDTOToSeries(SeriesDTO seriesDTO) {
        if ( seriesDTO == null ) {
            return null;
        }

        Series series = new Series();

        series.setId( seriesDTO.getId() );
        series.setTitle( seriesDTO.getTitle() );
        series.setDescription( seriesDTO.getDescription() );
        series.setStartYear( seriesDTO.getStartYear() );
        series.setEndYear( seriesDTO.getEndYear() );
        series.setMaturityRating( seriesDTO.getMaturityRating() );
        series.setImageUrl( seriesDTO.getImageUrl() );
        series.setAverageRating( seriesDTO.getAverageRating() );
        series.setViewCount( seriesDTO.getViewCount() );
        series.setFeatured( seriesDTO.getFeatured() );
        series.setCreatedAt( seriesDTO.getCreatedAt() );
        series.setUpdatedAt( seriesDTO.getUpdatedAt() );
        Set<String> set = seriesDTO.getGenres();
        if ( set != null ) {
            series.setGenres( new LinkedHashSet<String>( set ) );
        }
        series.setSeasons( seasonDTOListToSeasonList( seriesDTO.getSeasons() ) );

        return series;
    }

    @Override
    public void updateSeriesFromDTO(SeriesDTO dto, Series series) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            series.setId( dto.getId() );
        }
        if ( dto.getTitle() != null ) {
            series.setTitle( dto.getTitle() );
        }
        if ( dto.getDescription() != null ) {
            series.setDescription( dto.getDescription() );
        }
        if ( dto.getStartYear() != null ) {
            series.setStartYear( dto.getStartYear() );
        }
        if ( dto.getEndYear() != null ) {
            series.setEndYear( dto.getEndYear() );
        }
        if ( dto.getMaturityRating() != null ) {
            series.setMaturityRating( dto.getMaturityRating() );
        }
        if ( dto.getImageUrl() != null ) {
            series.setImageUrl( dto.getImageUrl() );
        }
        if ( dto.getAverageRating() != null ) {
            series.setAverageRating( dto.getAverageRating() );
        }
        if ( dto.getViewCount() != null ) {
            series.setViewCount( dto.getViewCount() );
        }
        if ( dto.getFeatured() != null ) {
            series.setFeatured( dto.getFeatured() );
        }
        if ( dto.getCreatedAt() != null ) {
            series.setCreatedAt( dto.getCreatedAt() );
        }
        if ( dto.getUpdatedAt() != null ) {
            series.setUpdatedAt( dto.getUpdatedAt() );
        }
        if ( series.getGenres() != null ) {
            Set<String> set = dto.getGenres();
            if ( set != null ) {
                series.getGenres().clear();
                series.getGenres().addAll( set );
            }
        }
        else {
            Set<String> set = dto.getGenres();
            if ( set != null ) {
                series.setGenres( new LinkedHashSet<String>( set ) );
            }
        }
        if ( series.getSeasons() != null ) {
            List<Season> list = seasonDTOListToSeasonList( dto.getSeasons() );
            if ( list != null ) {
                series.getSeasons().clear();
                series.getSeasons().addAll( list );
            }
        }
        else {
            List<Season> list = seasonDTOListToSeasonList( dto.getSeasons() );
            if ( list != null ) {
                series.setSeasons( list );
            }
        }
    }

    @Override
    public SeasonDTO seasonToSeasonDTO(Season season) {
        if ( season == null ) {
            return null;
        }

        SeasonDTO seasonDTO = new SeasonDTO();

        seasonDTO.setId( season.getId() );
        seasonDTO.setSeasonNumber( season.getSeasonNumber() );
        seasonDTO.setTitle( season.getTitle() );
        seasonDTO.setEpisodes( episodeListToEpisodeDTOList( season.getEpisodes() ) );

        return seasonDTO;
    }

    @Override
    public Season seasonDTOToSeason(SeasonDTO seasonDTO) {
        if ( seasonDTO == null ) {
            return null;
        }

        Season season = new Season();

        season.setId( seasonDTO.getId() );
        season.setSeasonNumber( seasonDTO.getSeasonNumber() );
        season.setTitle( seasonDTO.getTitle() );
        season.setEpisodes( episodeDTOListToEpisodeList( seasonDTO.getEpisodes() ) );

        return season;
    }

    @Override
    public void updateSeasonFromDTO(SeasonDTO dto, Season season) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            season.setId( dto.getId() );
        }
        if ( dto.getSeasonNumber() != null ) {
            season.setSeasonNumber( dto.getSeasonNumber() );
        }
        if ( dto.getTitle() != null ) {
            season.setTitle( dto.getTitle() );
        }
        if ( season.getEpisodes() != null ) {
            List<Episode> list = episodeDTOListToEpisodeList( dto.getEpisodes() );
            if ( list != null ) {
                season.getEpisodes().clear();
                season.getEpisodes().addAll( list );
            }
        }
        else {
            List<Episode> list = episodeDTOListToEpisodeList( dto.getEpisodes() );
            if ( list != null ) {
                season.setEpisodes( list );
            }
        }
    }

    @Override
    public EpisodeDTO episodeToEpisodeDTO(Episode episode) {
        if ( episode == null ) {
            return null;
        }

        EpisodeDTO episodeDTO = new EpisodeDTO();

        episodeDTO.setId( episode.getId() );
        episodeDTO.setEpisodeNumber( episode.getEpisodeNumber() );
        episodeDTO.setTitle( episode.getTitle() );
        episodeDTO.setDescription( episode.getDescription() );
        episodeDTO.setOverview( episode.getOverview() );
        episodeDTO.setDuration( episode.getDuration() );
        episodeDTO.setAirDate( episode.getAirDate() );
        episodeDTO.setImageUrl( episode.getImageUrl() );
        episodeDTO.setVideoUrl( episode.getVideoUrl() );
        episodeDTO.setCreatedAt( episode.getCreatedAt() );
        episodeDTO.setUpdatedAt( episode.getUpdatedAt() );

        mapSeasonToSeasonId( episodeDTO, episode );

        return episodeDTO;
    }

    @Override
    public Episode episodeDTOToEpisode(EpisodeDTO episodeDTO) {
        if ( episodeDTO == null ) {
            return null;
        }

        Episode episode = new Episode();

        episode.setId( episodeDTO.getId() );
        episode.setEpisodeNumber( episodeDTO.getEpisodeNumber() );
        episode.setTitle( episodeDTO.getTitle() );
        episode.setDescription( episodeDTO.getDescription() );
        episode.setOverview( episodeDTO.getOverview() );
        episode.setDuration( episodeDTO.getDuration() );
        episode.setAirDate( episodeDTO.getAirDate() );
        episode.setImageUrl( episodeDTO.getImageUrl() );
        episode.setVideoUrl( episodeDTO.getVideoUrl() );
        episode.setCreatedAt( episodeDTO.getCreatedAt() );
        episode.setUpdatedAt( episodeDTO.getUpdatedAt() );

        mapSeasonIdToSeason( episode, episodeDTO );

        return episode;
    }

    @Override
    public void updateEpisodeFromDTO(EpisodeDTO dto, Episode episode) {
        if ( dto == null ) {
            return;
        }

        if ( dto.getId() != null ) {
            episode.setId( dto.getId() );
        }
        if ( dto.getEpisodeNumber() != null ) {
            episode.setEpisodeNumber( dto.getEpisodeNumber() );
        }
        if ( dto.getTitle() != null ) {
            episode.setTitle( dto.getTitle() );
        }
        if ( dto.getDescription() != null ) {
            episode.setDescription( dto.getDescription() );
        }
        if ( dto.getOverview() != null ) {
            episode.setOverview( dto.getOverview() );
        }
        if ( dto.getDuration() != null ) {
            episode.setDuration( dto.getDuration() );
        }
        if ( dto.getAirDate() != null ) {
            episode.setAirDate( dto.getAirDate() );
        }
        if ( dto.getImageUrl() != null ) {
            episode.setImageUrl( dto.getImageUrl() );
        }
        if ( dto.getVideoUrl() != null ) {
            episode.setVideoUrl( dto.getVideoUrl() );
        }
        if ( dto.getCreatedAt() != null ) {
            episode.setCreatedAt( dto.getCreatedAt() );
        }
        if ( dto.getUpdatedAt() != null ) {
            episode.setUpdatedAt( dto.getUpdatedAt() );
        }

        mapSeasonIdToSeason( episode, dto );
    }

    protected List<SeasonDTO> seasonListToSeasonDTOList(List<Season> list) {
        if ( list == null ) {
            return null;
        }

        List<SeasonDTO> list1 = new ArrayList<SeasonDTO>( list.size() );
        for ( Season season : list ) {
            list1.add( seasonToSeasonDTO( season ) );
        }

        return list1;
    }

    protected List<Season> seasonDTOListToSeasonList(List<SeasonDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Season> list1 = new ArrayList<Season>( list.size() );
        for ( SeasonDTO seasonDTO : list ) {
            list1.add( seasonDTOToSeason( seasonDTO ) );
        }

        return list1;
    }

    protected List<EpisodeDTO> episodeListToEpisodeDTOList(List<Episode> list) {
        if ( list == null ) {
            return null;
        }

        List<EpisodeDTO> list1 = new ArrayList<EpisodeDTO>( list.size() );
        for ( Episode episode : list ) {
            list1.add( episodeToEpisodeDTO( episode ) );
        }

        return list1;
    }

    protected List<Episode> episodeDTOListToEpisodeList(List<EpisodeDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<Episode> list1 = new ArrayList<Episode>( list.size() );
        for ( EpisodeDTO episodeDTO : list ) {
            list1.add( episodeDTOToEpisode( episodeDTO ) );
        }

        return list1;
    }
}
