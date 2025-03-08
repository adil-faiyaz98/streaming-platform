package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class SeasonGraphQLController {

    @Autowired
    private CatalogService catalogService;

    @QueryMapping
    public List<SeasonDTO> seasons(@Argument String seriesId) {
        return catalogService.getSeasonsBySeriesId(Long.valueOf(seriesId));
    }

    @QueryMapping
    public SeasonDTO season(@Argument String id) {
        return catalogService.getSeasonById(Long.valueOf(id));
    }

    @QueryMapping
    public SeasonDTO seasonByNumber(@Argument String seriesId, @Argument Integer seasonNumber) {
        List<SeasonDTO> seasons = catalogService.getSeasonsBySeriesId(Long.valueOf(seriesId));
        return seasons.stream()
                .filter(s -> s.getSeasonNumber().equals(seasonNumber))
                .findFirst()
                .orElse(null);
    }

    @MutationMapping
    public SeasonDTO createSeason(@Argument String seriesId, @Argument("input") SeasonDTO seasonDTO) {
        return catalogService.createSeason(Long.valueOf(seriesId), seasonDTO);
    }

    @MutationMapping
    public SeasonDTO updateSeason(@Argument String id, @Argument("input") SeasonDTO seasonDTO) {
        return catalogService.updateSeason(Long.valueOf(id), seasonDTO);
    }

    @MutationMapping
    public Boolean deleteSeason(@Argument String id) {
        catalogService.deleteSeason(Long.valueOf(id));
        return true;
    }
}