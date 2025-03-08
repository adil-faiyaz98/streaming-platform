package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.Season;
import com.examples.streaming_platform.catalog.model.TvShow;
import com.examples.streaming_platform.catalog.repository.SeasonRepository;
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
public class SeasonService {

    private final SeasonRepository seasonRepository;
    private final TvShowRepository tvShowRepository;
    private final CatalogMapper catalogMapper;
    
    public List<SeasonDTO> getSeasonsByTvShowId(Long tvShowId) {
        if (!tvShowRepository.existsById(tvShowId)) {
            throw new ResourceNotFoundException("TvShow", "id", tvShowId);
        }
        
        return seasonRepository.findByTvShowIdOrderBySeasonNumber(tvShowId).stream()
                .map(catalogMapper::seasonToSeasonDTO)
                .collect(Collectors.toList());
    }
    
    public Page<SeasonDTO> getSeasonsByTvShowIdPaginated(Long tvShowId, Pageable pageable) {
        if (!tvShowRepository.existsById(tvShowId)) {
            throw new ResourceNotFoundException("TvShow", "id", tvShowId);
        }
        
        return seasonRepository.findByTvShowId(tvShowId, pageable)
                .map(catalogMapper::seasonToSeasonDTO);
    }
    
    public SeasonDTO getSeasonById(Long id) {
        Season season = seasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "id", id));
        return catalogMapper.seasonToSeasonDTO(season);
    }
    
    public SeasonDTO getSeasonByTvShowIdAndSeasonNumber(Long tvShowId, Integer seasonNumber) {
        Season season = seasonRepository.findByTvShowIdAndSeasonNumber(tvShowId, seasonNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "tvShowId and seasonNumber", tvShowId + " and " + seasonNumber));
        return catalogMapper.seasonToSeasonDTO(season);
    }
    
    @Transactional
    public SeasonDTO createSeason(Long tvShowId, SeasonDTO seasonDTO) {
        TvShow tvShow = tvShowRepository.findById(tvShowId)
                .orElseThrow(() -> new ResourceNotFoundException("TvShow", "id", tvShowId));
        
        Season season = catalogMapper.seasonDTOToSeason(seasonDTO);
        season.setTvShow(tvShow);
        
        Season savedSeason = seasonRepository.save(season);
        return catalogMapper.seasonToSeasonDTO(savedSeason);
    }
    
    @Transactional
    public SeasonDTO updateSeason(Long id, SeasonDTO seasonDTO) {
        Season existingSeason = seasonRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "id", id));
        
        Season updatedSeason = catalogMapper.seasonDTOToSeason(seasonDTO);
        updatedSeason.setId(existingSeason.getId());
        updatedSeason.setTvShow(existingSeason.getTvShow());
        
        Season savedSeason = seasonRepository.save(updatedSeason);
        return catalogMapper.seasonToSeasonDTO(savedSeason);
    }
    
    @Transactional
    public void deleteSeason(Long id) {
        if (!seasonRepository.existsById(id)) {
            throw new ResourceNotFoundException("Season", "id", id);
        }
        seasonRepository.deleteById(id);
    }
}