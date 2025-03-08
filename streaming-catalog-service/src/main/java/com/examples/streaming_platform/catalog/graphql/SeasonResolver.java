package main.java.com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.service.SeasonService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SeasonResolver {

    private final SeasonService seasonService;

    @QueryMapping
    public List<SeasonDTO> seasonsForSeries(@Argument String seriesId) {
        return seasonService.getSeasonsBySeriesId(Long.valueOf(seriesId));
    }

    @QueryMapping
    public SeasonDTO season(@Argument String id) {
        return seasonService.getSeasonById(Long.valueOf(id));
    }

    @MutationMapping
    public SeasonDTO createSeason(@Argument("input") SeasonDTO seasonInput) {
        return seasonService.createSeason(seasonInput.getSeriesId(), seasonInput);
    }

    @MutationMapping
    public SeasonDTO updateSeason(@Argument String id, @Argument("input") SeasonDTO seasonInput) {
        return seasonService.updateSeason(Long.valueOf(id), seasonInput);
    }

    @MutationMapping
    public Boolean deleteSeason(@Argument String id) {
        seasonService.deleteSeason(Long.valueOf(id));
        return true;
    }
}