package main.java.com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.Episode;
import com.examples.streaming_platform.catalog.model.Season;
import com.examples.streaming_platform.catalog.repository.EpisodeRepository;
import com.examples.streaming_platform.catalog.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final SeasonRepository seasonRepository;
    private final CatalogMapper catalogMapper;
    
    public List<EpisodeDTO> getEpisodesBySeasonId(Long seasonId) {
        if (!seasonRepository.existsById(seasonId)) {
            throw new ResourceNotFoundException("Season", "id", seasonId);
        }
        
        return episodeRepository.findBySeasonIdOrderByEpisodeNumber(seasonId).stream()
                .map(catalogMapper::episodeToEpisodeDTO)
                .collect(Collectors.toList());
    }
    
    public Page<EpisodeDTO> getEpisodesBySeasonIdPaginated(Long seasonId, Pageable pageable) {
        if (!seasonRepository.existsById(seasonId)) {
            throw new ResourceNotFoundException("Season", "id", seasonId);
        }
        
        return episodeRepository.findBySeasonId(seasonId, pageable)
                .map(catalogMapper::episodeToEpisodeDTO);
    }
    
    public EpisodeDTO getEpisodeById(Long id) {
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "id", id));
        return catalogMapper.episodeToEpisodeDTO(episode);
    }
    
    public EpisodeDTO getEpisodeBySeasonIdAndEpisodeNumber(Long seasonId, Integer episodeNumber) {
        Episode episode = episodeRepository.findBySeasonIdAndEpisodeNumber(seasonId, episodeNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "seasonId and episodeNumber", seasonId + " and " + episodeNumber));
        return catalogMapper.episodeToEpisodeDTO(episode);
    }
    
    @Transactional
    public EpisodeDTO createEpisode(Long seasonId, EpisodeDTO episodeDTO) {
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "id", seasonId));
        
        Episode episode = catalogMapper.episodeDTOToEpisode(episodeDTO);
        episode.setSeason(season);
        
        Episode savedEpisode = episodeRepository.save(episode);
        return catalogMapper.episodeToEpisodeDTO(savedEpisode);
    }
    
    @Transactional
    public EpisodeDTO updateEpisode(Long id, EpisodeDTO episodeDTO) {
        Episode existingEpisode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "id", id));
        
        Episode updatedEpisode = catalogMapper.episodeDTOToEpisode(episodeDTO);
        updatedEpisode.setId(existingEpisode.getId());
        updatedEpisode.setSeason(existingEpisode.getSeason());
        
        Episode savedEpisode = episodeRepository.save(updatedEpisode);
        return catalogMapper.episodeToEpisodeDTO(savedEpisode);
    }
    
    @Transactional
    public void deleteEpisode(Long id) {
        if (!episodeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Episode", "id", id);
        }
        episodeRepository.deleteById(id);
    }
}