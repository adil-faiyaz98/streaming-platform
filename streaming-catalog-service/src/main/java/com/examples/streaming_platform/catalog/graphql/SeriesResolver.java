package main.java.com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class SeriesResolver {

    private final SeriesService seriesService;

    @QueryMapping
    public Page<SeriesDTO> allSeries(@Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page, size);
        return seriesService.getAllSeries(pageable);
    }

    @QueryMapping
    public SeriesDTO series(@Argument String id) {
        return seriesService.getSeriesById(Long.valueOf(id));
    }

    @QueryMapping
    public Page<SeriesDTO> searchSeries(@Argument String title, @Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page, size);
        return seriesService.searchSeriesByTitle(title, pageable);
    }

    @QueryMapping
    public Page<SeriesDTO> seriesByGenre(@Argument String genre, @Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page, size);
        return seriesService.getSeriesByGenre(genre, pageable);
    }

    @QueryMapping
    public List<SeriesDTO> topRatedSeries() {
        return seriesService.getTopRatedSeries();
    }

    @QueryMapping
    public List<SeriesDTO> featuredSeries() {
        return seriesService.getFeaturedSeries();
    }

    @QueryMapping
    public List<Map<String, Object>> seriesGenreStats() {
        return seriesService.getGenreStatistics();
    }

    @MutationMapping
    public SeriesDTO createSeries(@Argument("input") SeriesDTO seriesInput) {
        return seriesService.createSeries(seriesInput);
    }

    @MutationMapping
    public SeriesDTO updateSeries(@Argument String id, @Argument("input") SeriesDTO seriesInput) {
        return seriesService.updateSeries(Long.valueOf(id), seriesInput);
    }

    @MutationMapping
    public Boolean deleteSeries(@Argument String id) {
        seriesService.deleteSeries(Long.valueOf(id));
        return true;
    }
}