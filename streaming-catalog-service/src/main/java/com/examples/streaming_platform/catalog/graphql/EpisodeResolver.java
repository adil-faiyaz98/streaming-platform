package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EpisodeResolver {

    private final CatalogService catalogService;

    @QueryMapping
    public EpisodeDTO episode(@Argument Long id) {
        return catalogService.getEpisodeById(id);
    }
    
    @QueryMapping
    public List<EpisodeDTO> episodesBySeasonId(@Argument Long seasonId) {
        return catalogService.getEpisodesBySeasonId(seasonId);
    }
}