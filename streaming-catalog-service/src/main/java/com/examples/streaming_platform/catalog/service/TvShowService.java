package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.TvShowDTO;
import com.examples.streaming_platform.catalog.graphql.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.TvShow;
import com.examples.streaming_platform.catalog.repository.TvShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TvShowService {

    private final TvShowRepository tvShowRepository;
    private final CatalogMapper catalogMapper;

    public Page<TvShowDTO> getAllTvShows(Pageable pageable) {
        log.debug("Fetching all TV Shows with pagination: {}", pageable);
        return tvShowRepository.findAll(pageable)
                .map(catalogMapper::tvShowToTvShowDTO);
    }

    public TvShowDTO getTvShowById(Long id) {
        log.debug("Fetching TV Show by ID: {}", id);
        TvShow tvShow = tvShowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TV Show", "id", id));
        return catalogMapper.tvShowToTvShowDTO(tvShow);
    }

    public Page<TvShowDTO> searchTvShowsByTitle(String title, Pageable pageable) {
        log.debug("Searching TV Shows with title: {}", title);
        return tvShowRepository.findByTitleContainingIgnoreCase(title, pageable)
                .map(catalogMapper::tvShowToTvShowDTO);
    }

    public List<TvShowDTO> getTopRatedTvShows() {
        log.debug("Fetching top-rated TV Shows");
        return tvShowRepository.findTop10ByOrderByRatingDesc().stream()
                .map(catalogMapper::tvShowToTvShowDTO)
                .collect(Collectors.toList());
    }

    public List<TvShowDTO> getFeaturedTvShows() {
        log.debug("Fetching featured TV Shows");
        return tvShowRepository.findByFeaturedTrue().stream()
                .map(catalogMapper::tvShowToTvShowDTO)
                .collect(Collectors.toList());
    }

    public Page<TvShowDTO> getTvShowsByGenre(String genre, Pageable pageable) {
        log.debug("Fetching TV Shows by genre: {}", genre);
        return tvShowRepository.findByGenre(genre, pageable)
                .map(catalogMapper::tvShowToTvShowDTO);
    }

    @Transactional
    public TvShowDTO createTvShow(TvShowDTO tvShowDTO) {
        log.debug("Creating new TV Show: {}", tvShowDTO.getTitle());
        TvShow tvShow = catalogMapper.tvShowDTOToTvShow(tvShowDTO);
        return catalogMapper.tvShowToTvShowDTO(tvShowRepository.save(tvShow));
    }

    @Transactional
    public TvShowDTO updateTvShow(Long id, TvShowDTO tvShowDTO) {
        log.debug("Updating TV Show with ID: {}", id);
        TvShow existingTvShow = tvShowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TV Show", "id", id));
        catalogMapper.updateTvShowFromDTO(tvShowDTO, existingTvShow);
        return catalogMapper.tvShowToTvShowDTO(tvShowRepository.save(existingTvShow));
    }

    @Transactional
    public void deleteTvShow(Long id) {
        log.debug("Deleting TV Show with ID: {}", id);
        if (!tvShowRepository.existsById(id)) {
            throw new ResourceNotFoundException("TV Show", "id", id);
        }
        tvShowRepository.deleteById(id);
    }

    @Transactional
    public TvShowDTO incrementViewCount(Long id) {
        log.debug("Incrementing view count for TV Show ID: {}", id);
        TvShow tvShow = tvShowRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TV Show", "id", id));
        tvShow.setViewCount(tvShow.getViewCount() + 1);
        tvShowRepository.save(tvShow);
        return catalogMapper.tvShowToTvShowDTO(tvShow);
    }
}
