package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class EpisodeGraphQLController {

    @Autowired
    private CatalogService catalogService;

    @QueryMapping
    public List<EpisodeDTO> episodes(@Argument String seasonId) {
        return catalogService.getEpisodesBySeasonId(Long.valueOf(seasonId));
    }

    @QueryMapping
    public EpisodeDTO episode(@Argument String id) {
        return catalogService.getEpisodeById(Long.valueOf(id));
    }

    @MutationMapping
    public EpisodeDTO createEpisode(@Argument String seasonId, @Argument("input") EpisodeDTO episodeDTO) {
        return catalogService.createEpisode(Long.valueOf(seasonId), episodeDTO);
    }

    @MutationMapping
    public EpisodeDTO updateEpisode(@Argument String id, @Argument("input") EpisodeDTO episodeDTO) {
        return catalogService.updateEpisode(Long.valueOf(id), episodeDTO);
    }

    @MutationMapping
    public Boolean deleteEpisode(@Argument String id) {
        catalogService.deleteEpisode(Long.valueOf(id));
        return true;
    }
}