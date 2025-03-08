package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.Series;
import com.examples.streaming_platform.catalog.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeriesService {

    private final SeriesRepository seriesRepository;
    private final CatalogMapper catalogMapper;
    
    public Page<SeriesDTO> getAllSeries(Pageable pageable) {
        return seriesRepository.findAll(pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }
    
    public SeriesDTO getSeriesById(Long id) {
        Series series = seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Series", "id", id));
        return catalogMapper.seriesToSeriesDTO(series);
    }
    
    public Page<SeriesDTO> searchSeriesByTitle(String title, Pageable pageable) {
        return seriesRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }
    
    public Page<SeriesDTO> getSeriesByGenre(String genre, Pageable pageable) {
        return seriesRepository.findByGenre(genre, pageable)
                .map(catalogMapper::seriesToSeriesDTO);
    }
    
    public List<SeriesDTO> getTopRatedSeries() {
        return seriesRepository.findTop10ByOrderByAverageRatingDesc()
                .stream()
                .map(catalogMapper::seriesToSeriesDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public SeriesDTO createSeries(SeriesDTO seriesDTO) {
        Series series = catalogMapper.seriesDTOToSeries(seriesDTO);
        Series savedSeries = seriesRepository.save(series);
        return catalogMapper.seriesToSeriesDTO(savedSeries);
    }
    
    @Transactional
    public SeriesDTO updateSeries(Long id, SeriesDTO seriesDTO) {
        Series existingSeries = seriesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Series", "id", id));
        
        catalogMapper.updateSeriesFromDTO(seriesDTO, existingSeries);
        Series updatedSeries = seriesRepository.save(existingSeries);
        return catalogMapper.seriesToSeriesDTO(updatedSeries);
    }
    
    @Transactional
    public void deleteSeries(Long id) {
        if (!seriesRepository.existsById(id)) {
            throw new ResourceNotFoundException("Series", "id", id);
        }
        seriesRepository.deleteById(id);
    }
}