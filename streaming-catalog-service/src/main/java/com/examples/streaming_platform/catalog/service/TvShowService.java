package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.TvShowDTO;
import com.examples.streaming_platform.catalog.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.TvShow;
import com.examples.streaming_platform.catalog.repository.TvShowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TvShowService {

    private final TvShowRepository tvShowRepository;
    private final CatalogMapper catalogMapper;
    
    public Page<TvShowDTO> getAllTvShows(Pageable pageable) {
        return tvShowRepository.findAll(pageable)
                .map(catalogMapper::tvShowToTvShowDTO);
    }
    
    public TvShowDTO getTvShowById(Long id) {
        TvShow tvShow = tvShowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TvShow", "id", id));
        return catalogMapper.tvShowToTvShowDTO(tvShow);
    }
    
    public Page<TvShowDTO> searchTvShowsByTitle(String title, Pageable pageable) {
        return tvShowRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(catalogMapper::tvShowToTvShowDTO);
    }
    
    public Page<TvShowDTO> getTvShowsByGenre(String genre, Pageable pageable) {
        return tvShowRepository.findByGenre(genre, pageable)
                .map(catalogMapper::tvShowToTvShowDTO);
    }
    
    public List<TvShowDTO> getTopRatedTvShows() {
        return tvShowRepository.findTop10ByOrderByRatingDesc()
                .stream()
                .map(catalogMapper::tvShowToTvShowDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public TvShowDTO createTvShow(TvShowDTO tvShowDTO) {
        TvShow tvShow = catalogMapper.tvShowDTOToTvShow(tvShowDTO);
        TvShow savedTvShow = tvShowRepository.save(tvShow);
        return catalogMapper.tvShowToTvShowDTO(savedTvShow);
    }
    
    @Transactional
    public TvShowDTO updateTvShow(Long id, TvShowDTO tvShowDTO) {
        TvShow existingTvShow = tvShowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TvShow", "id", id));
        
        catalogMapper.updateTvShowFromDTO(tvShowDTO, existingTvShow);
        TvShow updatedTvShow = tvShowRepository.save(existingTvShow);
        return catalogMapper.tvShowToTvShowDTO(updatedTvShow);
    }
    
    @Transactional
    public void deleteTvShow(Long id) {
        if (!tvShowRepository.existsById(id)) {
            throw new ResourceNotFoundException("TvShow", "id", id);
        }
        tvShowRepository.deleteById(id);
    }
}