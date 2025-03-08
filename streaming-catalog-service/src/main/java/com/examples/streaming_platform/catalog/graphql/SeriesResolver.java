package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SeriesResolver {

    private final CatalogService catalogService;

    @QueryMapping
    public Page<SeriesDTO> series(@Argument Integer page, @Argument Integer size) {
        return catalogService.getAllSeries(PageRequest.of(page != null ? page : 0, size != null ? size : 10));
    }
    
    @QueryMapping
    public SeriesDTO seriesById(@Argument Long id) {
        return catalogService.getSeriesById(id);
    }
    
    @QueryMapping
    public Page<SeriesDTO> searchSeries(@Argument String title, @Argument Integer page, @Argument Integer size) {
        return catalogService.searchSeriesByTitle(title, PageRequest.of(page != null ? page : 0, size != null ? size : 10));
    }
    
    @QueryMapping
    public Page<SeriesDTO> seriesByGenre(@Argument String genre, @Argument Integer page, @Argument Integer size) {
        return catalogService.getSeriesByGenre(genre, PageRequest.of(page != null ? page : 0, size != null ? size : 10));
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
    public SeriesDTO createSeries(@Argument SeriesDTO input) {
        return catalogService.createSeries(input);
    }
    
    @MutationMapping
    public SeriesDTO updateSeries(@Argument Long id, @Argument SeriesDTO input) {
        return catalogService.updateSeries(id, input);
    }
    
    @MutationMapping
    public Boolean deleteSeries(@Argument Long id) {
        catalogService.deleteSeries(id);
        return true;
    }
    
    // Field resolver for seasons in Series
    @SchemaMapping(typeName = "Series", field = "seasons")
    public List<SeasonDTO> getSeasons(SeriesDTO series) {
        return catalogService.getSeasonsBySeriesId(series.getId());
    }
}