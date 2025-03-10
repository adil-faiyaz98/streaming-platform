package com.examples.streaming_platform.catalog.graphql.controller;

import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SeriesGraphQLController {

    private final CatalogService catalogService;

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    public SeriesDTO series(@Argument Long id) {
        log.debug("Resolving series by ID: {}", id);
        return catalogService.getSeriesById(id);
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    public List<SeriesDTO> allSeries(@Argument int page, @Argument int size) {
        log.debug("Resolving all series, page: {}, size: {}", page, size);
        return catalogService.getAllSeries(PageRequest.of(page, size)).getContent();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    public List<SeriesDTO> searchSeries(@Argument String title, @Argument int page, @Argument int size) {
        log.debug("Searching series by title: {}, page: {}, size: {}", title, page, size);
        return catalogService.searchSeriesByTitle(title, PageRequest.of(page, size)).getContent();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    public List<SeriesDTO> seriesByGenre(@Argument String genre, @Argument int page, @Argument int size) {
        log.debug("Fetching series by genre: {}, page: {}, size: {}", genre, page, size);
        return catalogService.getSeriesByGenre(genre, PageRequest.of(page, size)).getContent();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    public List<SeriesDTO> topRatedSeries() {
        log.debug("Resolving top-rated series");
        return catalogService.getTopRatedSeries();
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    public List<SeriesDTO> featuredSeries() {
        log.debug("Resolving featured series");
        return catalogService.getFeaturedSeries();
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_write:series')")
    public SeriesDTO createSeries(@Argument("input") SeriesDTO seriesDTO) {
        log.debug("Creating series: {}", seriesDTO);
        return catalogService.createSeries(seriesDTO);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_write:series')")
    public SeriesDTO updateSeries(@Argument Long id, @Argument("input") SeriesDTO seriesDTO) {
        log.debug("Updating series ID: {}, data: {}", id, seriesDTO);
        return catalogService.updateSeries(id, seriesDTO);
    }

    @MutationMapping
    @PreAuthorize("hasAuthority('SCOPE_delete:series')")
    public Boolean deleteSeries(@Argument Long id) {
        log.debug("Deleting series ID: {}", id);
        catalogService.deleteSeries(id);
        return true;
    }

    @QueryMapping
    @PreAuthorize("hasAuthority('SCOPE_read:series')")
    public Map<String, Object> seriesPage(@Argument Integer page, @Argument Integer size) {
        int pageNumber = page != null ? page : 0;
        int pageSize = size != null ? size : 10;

        log.debug("Fetching series page {}, size: {}", pageNumber, pageSize);
        Page<SeriesDTO> seriesPage = catalogService.getAllSeries(PageRequest.of(pageNumber, pageSize));

        Map<String, Object> result = new HashMap<>();
        result.put("content", seriesPage.getContent());
        result.put("totalElements", seriesPage.getTotalElements());
        result.put("totalPages", seriesPage.getTotalPages());

        return result;
    }
}
