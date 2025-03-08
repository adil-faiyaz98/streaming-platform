package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SeriesGraphQLController {

    @Autowired
    private CatalogService catalogService;

    @QueryMapping
    public SeriesDTO series(@Argument String id) {
        return catalogService.getSeriesById(Long.valueOf(id));
    }

    @QueryMapping
    public List<SeriesDTO> allSeries() {
        return catalogService.getAllSeries(PageRequest.of(0, 10)).getContent();
    }

    @QueryMapping
    public List<SeriesDTO> searchSeries(@Argument String title) {
        return catalogService.searchSeriesByTitle(title, PageRequest.of(0, 10)).getContent();
    }

    @QueryMapping
    public List<SeriesDTO> seriesByGenre(@Argument String genre) {
        return catalogService.getSeriesByGenre(genre, PageRequest.of(0, 10)).getContent();
    }

    @QueryMapping
    public List<SeriesDTO> topRatedSeries() {
        return catalogService.getTopRatedSeries();
    }

    @QueryMapping
    public List<SeriesDTO> featuredSeries() {
        return catalogService.getFeaturedSeries();
    }

    @MutationMapping
    public SeriesDTO createSeries(@Argument("input") SeriesDTO seriesDTO) {
        return catalogService.createSeries(seriesDTO);
    }

    @MutationMapping
    public SeriesDTO updateSeries(@Argument String id, @Argument("input") SeriesDTO seriesDTO) {
        return catalogService.updateSeries(Long.valueOf(id), seriesDTO);
    }

    @MutationMapping
    public Boolean deleteSeries(@Argument String id) {
        catalogService.deleteSeries(Long.valueOf(id));
        return true;
    }

    @QueryMapping
    public Map<String, Object> seriesPage(@Argument Integer page, @Argument Integer size) {
        page = page == null ? 0 : page;
        size = size == null ? 10 : size;
        
        Page<SeriesDTO> seriesPage = catalogService.getAllSeries(PageRequest.of(page, size));
        
        Map<String, Object> result = new HashMap<>();
        result.put("content", seriesPage.getContent());
        result.put("totalElements", seriesPage.getTotalElements());
        result.put("totalPages", seriesPage.getTotalPages());
        
        return result;
    }
}