package com.examples.streaming_platform.catalog.graphql.controller;

import com.examples.streaming_platform.catalog.dto.TvShowDTO;
import com.examples.streaming_platform.catalog.service.TvShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class TvShowGraphQLController {

    private final TvShowService tvShowService;

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:tvshows')")
    public Page<TvShowDTO> tvShows(Pageable pageable) {
        log.debug("Fetching all TV Shows with pagination: {}", pageable);
        return tvShowService.getAllTvShows(pageable);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:tvshows')")
    public TvShowDTO tvShow(@Argument Long id) {
        log.debug("Fetching TV show by ID: {}", id);
        return tvShowService.getTvShowById(id);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:tvshows')")
    public Page<TvShowDTO> searchTvShows(@Argument String title, Pageable pageable) {
        log.debug("Searching TV shows by title: {}", title);
        return tvShowService.searchTvShowsByTitle(title, pageable);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:tvshows')")
    public Page<TvShowDTO> tvShowsByGenre(@Argument String genre, Pageable pageable) {
        log.debug("Fetching TV shows by genre: {}", genre);
        return tvShowService.getTvShowsByGenre(genre, pageable);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:tvshows')")
    public List<TvShowDTO> topRatedTvShows() {
        log.debug("Fetching top-rated TV shows");
        return tvShowService.getTopRatedTvShows();
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_write:tvshows')")
    public TvShowDTO createTvShow(@Argument("input") TvShowDTO tvShowDTO) {
        log.debug("Creating new TV show: {}", tvShowDTO.getTitle());
        return tvShowService.createTvShow(tvShowDTO);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_write:tvshows')")
    public TvShowDTO updateTvShow(@Argument Long id, @Argument("input") TvShowDTO tvShowDTO) {
        log.debug("Updating TV show ID: {}", id);
        return tvShowService.updateTvShow(id, tvShowDTO);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_delete:tvshows')")
    public Boolean deleteTvShow(@Argument Long id) {
        log.debug("Deleting TV show ID: {}", id);
        tvShowService.deleteTvShow(id);
        return true;
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_write:tvshows')")
    public TvShowDTO incrementTvShowViewCount(@Argument Long id) {
        log.debug("Incrementing view count for TV show ID: {}", id);
        return tvShowService.incrementViewCount(id);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:tvshows')")
    public List<TvShowDTO> featuredTvShows() {
        log.debug("Fetching featured TV shows");
        return tvShowService.getFeaturedTvShows();
    }
}
