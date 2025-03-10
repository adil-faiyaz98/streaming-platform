package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.graphql.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.Episode;
import com.examples.streaming_platform.catalog.model.Season;
import com.examples.streaming_platform.catalog.repository.EpisodeRepository;
import com.examples.streaming_platform.catalog.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EpisodeService {

    private final EpisodeRepository episodeRepository;
    private final SeasonRepository seasonRepository;
    private final CatalogMapper catalogMapper;

    public List<EpisodeDTO> getEpisodesBySeasonId(Long seasonId) {
        log.debug("Fetching episodes for season ID: {}", seasonId);
        if (!seasonRepository.existsById(seasonId)) {
            throw new ResourceNotFoundException("Season", "id", seasonId);
        }
        return episodeRepository.findBySeasonIdOrderByEpisodeNumber(seasonId)
                .stream()
                .map(catalogMapper::episodeToEpisodeDTO)
                .toList();
    }

    public Page<EpisodeDTO> getEpisodesBySeasonIdPaginated(Long seasonId, Pageable pageable) {
        log.debug("Fetching paginated episodes for season ID: {}", seasonId);
        if (!seasonRepository.existsById(seasonId)) {
            throw new ResourceNotFoundException("Season", "id", seasonId);
        }
        return episodeRepository.findBySeasonIdOrderByEpisodeNumber(seasonId, pageable)
                .map(catalogMapper::episodeToEpisodeDTO);
    }

    public EpisodeDTO getEpisodeById(Long id) {
        log.debug("Fetching episode with ID: {}", id);
        Episode episode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "id", id));
        return catalogMapper.episodeToEpisodeDTO(episode);
    }

    public EpisodeDTO getEpisodeBySeasonIdAndEpisodeNumber(Long seasonId, Integer episodeNumber) {
        log.debug("Fetching episode for season ID: {}, episode number: {}", seasonId, episodeNumber);
        Episode episode = episodeRepository.findBySeasonIdAndEpisodeNumber(seasonId, episodeNumber)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Episode",
                        "seasonId and episodeNumber",
                        seasonId + " and " + episodeNumber
                ));
        return catalogMapper.episodeToEpisodeDTO(episode);
    }

    @Transactional
    public EpisodeDTO createEpisode(Long seasonId, EpisodeDTO episodeDTO) {
        log.debug("Creating episode for season ID: {}, title: {}", seasonId, episodeDTO.getTitle());
        Season season = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new ResourceNotFoundException("Season", "id", seasonId));

        Episode episode = catalogMapper.episodeDTOToEpisode(episodeDTO);
        episode.setSeason(season);
        Episode savedEpisode = episodeRepository.save(episode);
        return catalogMapper.episodeToEpisodeDTO(savedEpisode);
    }

    @Transactional
    public EpisodeDTO updateEpisode(Long id, EpisodeDTO episodeDTO) {
        log.debug("Updating episode ID: {}", id);
        Episode existingEpisode = episodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Episode", "id", id));

        catalogMapper.updateEpisodeFromDTO(episodeDTO, existingEpisode);
        Episode updatedEpisode = episodeRepository.save(existingEpisode);
        return catalogMapper.episodeToEpisodeDTO(updatedEpisode);
    }

    @Transactional
    public void deleteEpisode(Long id) {
        log.debug("Deleting episode ID: {}", id);
        if (!episodeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Episode", "id", id);
        }
        episodeRepository.deleteById(id);
    }
}
