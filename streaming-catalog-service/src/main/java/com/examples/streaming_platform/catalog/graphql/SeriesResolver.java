package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.service.SeriesService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SeriesResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final SeriesService seriesService;

    // Queries
    public SeriesDTO getSeries(Long id) {
        return seriesService.getSeriesById(id);
    }

    public Map<String, Object> getAllSeries(Integer page, Integer size, Optional<String> title, Optional<String> genre) {
        PageRequest pageRequest = PageRequest.of(page != null ? page : 0, size != null ? size : 20);
        
        Page<SeriesDTO> seriesPage;
        if (title.isPresent() && !title.get().isEmpty()) {
            seriesPage = seriesService.searchSeriesByTitle(title.get(), pageRequest);
        } else if (genre.isPresent() && !genre.get().isEmpty()) {
            seriesPage = seriesService.getSeriesByGenre(genre.get(), pageRequest);
        } else {
            seriesPage = seriesService.getAllSeries(pageRequest);
        }
        
        return Map.of(
                "content", seriesPage.getContent(),
                "totalElements", seriesPage.getTotalElements(),
                "totalPages", seriesPage.getTotalPages(),
                "size", seriesPage.getSize(),
                "number", seriesPage.getNumber()
        );
    }

    public List<SeriesDTO> getTopRatedSeries() {
        return seriesService.getTopRatedSeries();
    }

    // Mutations
    public SeriesDTO createSeries(SeriesDTO seriesInput) {
        return seriesService.createSeries(seriesInput);
    }

    public SeriesDTO updateSeries(Long id, SeriesDTO seriesInput) {
        return seriesService.updateSeries(id, seriesInput);
    }

    public Boolean deleteSeries(Long id) {
        try {
            seriesService.deleteSeries(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}