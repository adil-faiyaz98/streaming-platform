package main.java.com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.service.SeriesService;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@DgsComponent
@RequiredArgsConstructor
public class SeriesDataFetcher {

    private final SeriesService seriesService;

    @DgsQuery
    public List<SeriesDTO> series(@InputArgument Integer page, @InputArgument Integer size) {
        int pageNum = Optional.ofNullable(page).orElse(0);
        int pageSize = Optional.ofNullable(size).orElse(20);
        return seriesService.getAllSeries(PageRequest.of(pageNum, pageSize)).getContent();
    }

    @DgsQuery
    public SeriesDTO seriesById(@InputArgument String id) {
        return seriesService.getSeriesById(Long.parseLong(id));
    }

    @DgsQuery
    public List<SeriesDTO> seriesByTitle(@InputArgument String title, 
                                         @InputArgument Integer page, 
                                         @InputArgument Integer size) {
        int pageNum = Optional.ofNullable(page).orElse(0);
        int pageSize = Optional.ofNullable(size).orElse(20);
        return seriesService.searchSeriesByTitle(title, PageRequest.of(pageNum, pageSize)).getContent();
    }

    @DgsQuery
    public List<SeriesDTO> seriesByGenre(@InputArgument String genre, 
                                         @InputArgument Integer page, 
                                         @InputArgument Integer size) {
        int pageNum = Optional.ofNullable(page).orElse(0);
        int pageSize = Optional.ofNullable(size).orElse(20);
        return seriesService.getSeriesByGenre(genre, PageRequest.of(pageNum, pageSize)).getContent();
    }

    @DgsQuery
    public List<SeriesDTO> topRatedSeries() {
        return seriesService.getTopRatedSeries();
    }

    @DgsMutation
    public SeriesDTO createSeries(@InputArgument("input") SeriesDTO seriesDTO) {
        return seriesService.createSeries(seriesDTO);
    }

    @DgsMutation
    public SeriesDTO updateSeries(@InputArgument String id, @InputArgument("input") SeriesDTO seriesDTO) {
        return seriesService.updateSeries(Long.parseLong(id), seriesDTO);
    }

    @DgsMutation
    public Boolean deleteSeries(@InputArgument String id) {
        seriesService.deleteSeries(Long.parseLong(id));
        return true;
    }

    @DgsQuery
    public Map<String, Object> seriesPage(@InputArgument Integer page, @InputArgument Integer size) {
        int pageNum = Optional.ofNullable(page).orElse(0);
        int pageSize = Optional.ofNullable(size).orElse(20);
        
        Page<SeriesDTO> seriesPage = seriesService.getAllSeries(PageRequest.of(pageNum, pageSize));
        
        return Map.of(
            "content", seriesPage.getContent(),
            "totalElements", seriesPage.getTotalElements(),
            "totalPages", seriesPage.getTotalPages(),
            "size", seriesPage.getSize(),
            "number", seriesPage.getNumber()
        );
    }
}