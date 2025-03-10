package com.examples.streaming_platform.catalog.graphql.resolver;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EpisodeResolver {

    private final CatalogService catalogService;

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:episodes')")
    public EpisodeDTO episode(@Argument Long id) {
        log.debug("Resolving GraphQL query for episode ID: {}", id);
        return catalogService.getEpisodeById(id);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:episodes')")
    public List<EpisodeDTO> episodesBySeasonId(@Argument Long seasonId) {
        log.debug("Resolving GraphQL query for episodes by season ID: {}", seasonId);
        return catalogService.getEpisodesBySeasonId(seasonId);
    }
}
