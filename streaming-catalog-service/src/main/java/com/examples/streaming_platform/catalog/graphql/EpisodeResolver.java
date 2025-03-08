package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.service.EpisodeService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EpisodeResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final EpisodeService episodeService;

    // Queries
    public EpisodeDTO getEpisode(Long id) {
        return episodeService.getEpisodeById(id);
    }

    public List<EpisodeDTO> getEpisodesBySeasonId(Long seasonId) {
        return episodeService.getEpisodesBySeasonId(seasonId);
    }

    // Mutations
    public EpisodeDTO createEpisode(EpisodeDTO episodeInput) {
        return episodeService.createEpisode(episodeInput.getSeasonId(), episodeInput);
    }

    public EpisodeDTO updateEpisode(Long id, EpisodeDTO episodeInput) {
        return episodeService.updateEpisode(id, episodeInput);
    }

    public Boolean deleteEpisode(Long id) {
        try {
            episodeService.deleteEpisode(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}