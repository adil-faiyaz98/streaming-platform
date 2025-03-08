package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.service.SeasonService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

@DgsComponent
@RequiredArgsConstructor
public class SeasonDataFetcher {

    private final SeasonService seasonService;

    @DgsQuery
    public List<SeasonDTO> seasons(@InputArgument String seriesId) {
        return seasonService.getSeasonsBySeriesId(Long.parseLong(seriesId));
    }

    @DgsQuery
    public SeasonDTO season(@InputArgument String id) {
        return seasonService.getSeasonById(Long.parseLong(id));
    }

    @DgsQuery
    public SeasonDTO seasonByNumber(@InputArgument String seriesId, @InputArgument Integer seasonNumber) {
        return seasonService.getSeasonBySeriesIdAndSeasonNumber(Long.parseLong(seriesId), seasonNumber);
    }

    @DgsMutation
    public SeasonDTO createSeason(@InputArgument("input") SeasonDTO seasonDTO) {
        return seasonService.createSeason(seasonDTO.getSeriesId(), seasonDTO);
    }

    @DgsMutation
    public SeasonDTO updateSeason(@InputArgument String id, @InputArgument("input") SeasonDTO seasonDTO) {
        return seasonService.updateSeason(Long.parseLong(id), seasonDTO);
    }

    @DgsMutation
    public Boolean deleteSeason(@InputArgument String id) {
        seasonService.deleteSeason(Long.parseLong(id));
        return true;
    }
}