package com.examples.streaming_platform.catalog.graphql.resolver;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SeasonResolver {

    private final CatalogService catalogService;

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:seasons')")
    public SeasonDTO season(@Argument Long id) {
        log.debug("Resolving season by ID: {}", id);
        return catalogService.getSeasonById(id);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:seasons')")
    public List<SeasonDTO> seasonsBySeriesId(@Argument Long seriesId) {
        log.debug("Resolving seasons for series ID: {}", seriesId);
        return catalogService.getSeasonsBySeriesId(seriesId);
    }

    @SchemaMapping(typeName = "Season", field = "episodes")
    @PreAuthorize("hasAuthority('SCOPE_read:episodes')")
    public List<EpisodeDTO> getEpisodes(SeasonDTO season) {
        log.debug("Resolving episodes for season ID: {}", season.getId());
        return catalogService.getEpisodesBySeasonId(season.getId());
    }
}
