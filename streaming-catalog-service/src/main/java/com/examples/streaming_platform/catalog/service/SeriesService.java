package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.graphql.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.Series;
import com.examples.streaming_platform.catalog.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final CatalogMapper catalogMapper;

    @Transactional(readOnly = true)
    @Cacheable(value = "series", key = "'page_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public Page<SeriesDTO> getAllSeries(Pageable pageable) {
        log.debug("Fetching all series with pagination: {}", pageable);
        return seriesRepository.findAll(pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }

    @Transactional(readOnly = true)
    public SeriesDTO getSeriesById(Long id) {
        log.debug("Fetching series by ID: {}", id);
        return seriesRepository.findById(id)
                .map(catalogMapper::seriesToSeriesDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Series", "id", id));
    }

    @Transactional(readOnly = true)
    public Page<SeriesDTO> searchSeriesByTitle(String title, Pageable pageable) {
        log.debug("Searching series by title: {}", title);
        return seriesRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }

    @Transactional(readOnly = true)
    public Page<SeriesDTO> getSeriesByGenre(String genre, Pageable pageable) {
        log.debug("Fetching series by genre: {}", genre);
        return seriesRepository.findByGenresContainingIgnoreCase(genre, pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }

    @Transactional(readOnly = true)
    @Cacheable("topRatedSeries")
    public List<SeriesDTO> getTopRatedSeries() {
        log.debug("Fetching top-rated series");
        return seriesRepository.findTop10ByOrderByAverageRatingDesc().stream()
                .map(catalogMapper::seriesToSeriesDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SeriesDTO> getFeaturedSeries() {
        log.debug("Fetching featured series");
        return seriesRepository.findByFeaturedTrue().stream()
                .map(catalogMapper::seriesToSeriesDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SeriesDTO createSeries(SeriesDTO seriesDTO) {
        log.debug("Creating new series: {}", seriesDTO.getTitle());
        Series series = catalogMapper.seriesDTOToSeries(seriesDTO);
        Series savedSeries = seriesRepository.save(series);
        return catalogMapper.seriesToSeriesDTO(savedSeries);
    }

    @Transactional
    public SeriesDTO updateSeries(Long id, SeriesDTO seriesDTO) {
        log.debug("Updating series with id: {}", id);
        Series existingSeries = seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Series", "id", id));
        catalogMapper.updateSeriesFromDTO(seriesDTO, existingSeries);
        Series updatedSeries = seriesRepository.save(existingSeries);
        return catalogMapper.seriesToSeriesDTO(updatedSeries);
    }

    @Transactional
    public void deleteSeries(Long id) {
        log.debug("Deleting series with id: {}", id);
        if (!seriesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Series", "id", id);
        }
        seriesRepository.deleteById(id);
    }

    @Transactional
    public void incrementSeriesViewCount(Long id) {
        log.debug("Incrementing view count for series ID: {}", id);
        seriesRepository.incrementViewCount(id);
    }
}
