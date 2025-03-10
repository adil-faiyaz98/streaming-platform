package com.examples.streaming_platform.catalog.graphql.controller;

import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SeasonGraphQLController {

    private final CatalogService catalogService;

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:seasons')")
    public List<SeasonDTO> seasons(@Argument Long seriesId) {
        log.debug("Resolving seasons for series ID: {}", seriesId);
        return catalogService.getSeasonsBySeriesId(seriesId);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:seasons')")
    public SeasonDTO season(@Argument Long id) {
        log.debug("Resolving season by ID: {}", id);
        return catalogService.getSeasonById(id);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:seasons')")
    public SeasonDTO seasonByNumber(@Argument Long seriesId, @Argument Integer seasonNumber) {
        log.debug("Resolving season by series ID: {} and season number: {}", seriesId, seasonNumber);
        return catalogService.getSeasonsBySeriesId(seriesId).stream()
                .filter(s -> s.getSeasonNumber().equals(seasonNumber))
                .findFirst()
                .orElse(null);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_write:seasons')")
    public SeasonDTO createSeason(@Argument Long seriesId, @Argument("input") SeasonDTO seasonDTO) {
        log.debug("Creating season for series ID: {}, data: {}", seriesId, seasonDTO);
        return catalogService.createSeason(seriesId, seasonDTO);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_write:seasons')")
    public SeasonDTO updateSeason(@Argument Long id, @Argument("input") SeasonDTO seasonDTO) {
        log.debug("Updating season ID: {}, data: {}", id, seasonDTO);
        return catalogService.updateSeason(id, seasonDTO);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_delete:seasons')")
    public Boolean deleteSeason(@Argument Long id) {
        log.debug("Deleting season ID: {}", id);
        catalogService.deleteSeason(id);
        return true;
    }
}
