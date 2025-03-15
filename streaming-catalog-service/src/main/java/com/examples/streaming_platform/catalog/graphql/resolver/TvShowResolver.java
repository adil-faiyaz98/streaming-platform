package com.examples.streaming_platform.catalog.graphql.resolver;

import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.dto.TvShowDTO;
import com.examples.streaming_platform.catalog.service.SeasonService;
import com.examples.streaming_platform.catalog.service.TvShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class TvShowResolver {

    private final TvShowService tvShowService;
    private final SeasonService seasonService;

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:tvshows')")
    public TvShowDTO tvShow(@Argument Long id) {
        log.debug("Resolving GraphQL query for TV show ID: {}", id);
        return tvShowService.getTvShowById(id);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:seasons')")
    public List<SeasonDTO> seasons(@Argument Long tvShowId) {
        log.debug("Resolving GraphQL query for seasons of TV show ID: {}", tvShowId);
        return seasonService.getSeasonsByTvShowId(tvShowId);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:tvshows')")
    public List<TvShowDTO> topRatedTvShows() {
        log.debug("Resolving GraphQL query for top-rated TV shows");
        return tvShowService.getTopRatedTvShows();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:tvshows')")
    public List<TvShowDTO> featuredTvShows() {
        log.debug("Resolving GraphQL query for featured TV shows");
        return tvShowService.getFeaturedTvShows();
    }
}
