package com.examples.streaming_platform.catalog.graphql.controller;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
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
public class EpisodeGraphQLController {

    private final CatalogService catalogService;

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:episodes')")
    public List<EpisodeDTO> episodes(@Argument Long seasonId) {
        log.debug("Fetching episodes for season ID: {}", seasonId);
        return catalogService.getEpisodesBySeasonId(seasonId);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:episodes')")
    public EpisodeDTO episode(@Argument Long id) {
        log.debug("Fetching episode by ID: {}", id);
        return catalogService.getEpisodeById(id);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_write:episodes')")
    public EpisodeDTO createEpisode(@Argument Long seasonId, @Argument("input") EpisodeDTO episodeDTO) {
        log.debug("Creating episode for season ID: {}, data: {}", seasonId, episodeDTO);
        return catalogService.createEpisode(seasonId, episodeDTO);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_write:episodes')")
    public EpisodeDTO updateEpisode(@Argument Long id, @Argument("input") EpisodeDTO episodeDTO) {
        log.debug("Updating episode ID: {}, data: {}", id, episodeDTO);
        return catalogService.updateEpisode(id, episodeDTO);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_delete:episodes')")
    public Boolean deleteEpisode(@Argument Long id) {
        log.debug("Deleting episode ID: {}", id);
        catalogService.deleteEpisode(id);
        return true;
    }
}
