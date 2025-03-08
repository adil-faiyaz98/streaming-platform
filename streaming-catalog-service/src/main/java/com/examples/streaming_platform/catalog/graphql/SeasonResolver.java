package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SeasonResolver {

    private final CatalogService catalogService;

    @QueryMapping
    public SeasonDTO season(@Argument Long id) {
        return catalogService.getSeasonById(id);
    }
    
    @QueryMapping
    public List<SeasonDTO> seasonsBySeriesId(@Argument Long seriesId) {
        return catalogService.getSeasonsBySeriesId(seriesId);
    }
    
    // Field resolver for episodes in Season
    @SchemaMapping(typeName = "Season", field = "episodes")
    public List<EpisodeDTO> getEpisodes(SeasonDTO season) {
        return catalogService.getEpisodesBySeasonId(season.getId());
    }
}