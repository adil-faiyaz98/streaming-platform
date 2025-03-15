package com.examples.streaming_platform.catalog.graphql.resolver;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class SeriesResolver {

    private final CatalogService catalogService;

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    public SeriesDTO seriesById(@Argument Long id) {
        log.debug("Resolving series by ID: {}", id);
        return catalogService.getSeriesById(id);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    public Page<SeriesDTO> seriesPage(
            @Argument Integer page,
            @Argument Integer size,
            @Argument String title,
            @Argument String genre) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 10;

        log.debug("Resolving series page: {}, size: {}, title: {}, genre: {}", pageNumber, pageSize, title, genre);

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);

        if (title != null && !title.isBlank()) {
            return catalogService.searchSeriesByTitle(title, pageRequest);
        } else if (genre != null && !genre.isBlank()) {
            return catalogService.getSeriesByGenre(genre, pageRequest);
        } else {
            return catalogService.getAllSeries(pageRequest);
        }
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    public List<SeriesDTO> topRatedSeries() {
        log.debug("Resolving top-rated series");
        return catalogService.getTopRatedSeries();
    }

    @SchemaMapping(typeName = "Series", field = "seasons")
    @PreAuthorize("hasAuthority('SCOPE_read:seasons')")
    public List<SeasonDTO> getSeasons(SeriesDTO series) {
        log.debug("Resolving seasons for series ID: {}", series.getId());
        return catalogService.getSeasonsBySeriesId(series.getId());
    }

    @SchemaMapping(typeName = "Series", field = "episodes")
    @PreAuthorize("hasAuthority('SCOPE_read:episodes')")
    public List<EpisodeDTO> getEpisodes(SeriesDTO series) {
        log.debug("Resolving episodes for series ID: {}", series.getId());
        return catalogService.getEpisodesBySeriesId(series.getId());
    }
}
