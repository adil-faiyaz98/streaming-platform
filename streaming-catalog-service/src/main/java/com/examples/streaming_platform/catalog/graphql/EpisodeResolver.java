package main.java.com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EpisodeResolver {

    private final EpisodeService episodeService;

    @QueryMapping
    public List<EpisodeDTO> episodesForSeason(@Argument String seasonId) {
        return episodeService.getEpisodesBySeasonId(Long.valueOf(seasonId));
    }

    @QueryMapping
    public EpisodeDTO episode(@Argument String id) {
        return episodeService.getEpisodeById(Long.valueOf(id));
    }

    @MutationMapping
    public EpisodeDTO createEpisode(@Argument("input") EpisodeDTO episodeInput) {
        return episodeService.createEpisode(episodeInput.getSeasonId(), episodeInput);
    }

    @MutationMapping
    public EpisodeDTO updateEpisode(@Argument String id, @Argument("input") EpisodeDTO episodeInput) {
        return episodeService.updateEpisode(Long.valueOf(id), episodeInput);
    }

    @MutationMapping
    public Boolean deleteEpisode(@Argument String id) {
        episodeService.deleteEpisode(Long.valueOf(id));
        return true;
    }
}