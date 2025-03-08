package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.service.SeasonService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SeasonResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final SeasonService seasonService;

    // Queries
    public SeasonDTO getSeason(Long id) {
        return seasonService.getSeasonById(id);
    }

    public List<SeasonDTO> getSeasonsBySeriesId(Long seriesId) {
        return seasonService.getSeasonsBySeriesId(seriesId);
    }

    // Mutations
    public SeasonDTO createSeason(SeasonDTO seasonInput) {
        return seasonService.createSeason(seasonInput.getSeriesId(), seasonInput);
    }

    public SeasonDTO updateSeason(Long id, SeasonDTO seasonInput) {
        return seasonService.updateSeason(id, seasonInput);
    }

    public Boolean deleteSeason(Long id) {
        try {
            seasonService.deleteSeason(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}