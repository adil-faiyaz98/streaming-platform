package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.service.EpisodeService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;

import java.util.List;

@DgsComponent
@RequiredArgsConstructor
public class EpisodeDataFetcher {

    private final EpisodeService episodeService;

    @DgsQuery
    public List<EpisodeDTO> episodes(@InputArgument String seasonId) {
        return episodeService.getEpisodesBySeasonId(Long.parseLong(seasonId));
    }

    @DgsQuery
    public EpisodeDTO episode(@InputArgument String id) {
        return episodeService.getEpisodeById(Long.parseLong(id));
    }

    @DgsQuery
    public EpisodeDTO episodeByNumber(@InputArgument String seasonId, @InputArgument Integer episodeNumber) {
        return episodeService.getEpisodeBySeasonIdAndEpisodeNumber(Long.parseLong(seasonId), episodeNumber);
    }

    @DgsMutation
    public EpisodeDTO createEpisode(@InputArgument("input") EpisodeDTO episodeDTO) {
        return episodeService.createEpisode(episodeDTO.getSeasonId(), episodeDTO);
    }

    @DgsMutation
    public EpisodeDTO updateEpisode(@InputArgument String id, @InputArgument("input") EpisodeDTO episodeDTO) {
        return episodeService.updateEpisode(Long.parseLong(id), episodeDTO);
    }

    @DgsMutation
    public Boolean deleteEpisode(@InputArgument String id) {
        episodeService.deleteEpisode(Long.parseLong(id));
        return true;
    }
}