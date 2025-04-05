package com.examples.streaming_platform.recommendation.service;

import com.examples.streaming_platform.recommendation.dto.ContentItemDTO;
import com.examples.streaming_platform.recommendation.model.ContentItem;
import com.examples.streaming_platform.recommendation.repository.ContentItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Service for managing content items.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ContentItemService {

    private final ContentItemRepository contentItemRepository;

    /**
     * Get a content item by ID.
     *
     * @param id the item ID
     * @return the content item, if found
     */
    @Transactional(readOnly = true)
    public Optional<ContentItem> getContentItem(String id) {
        return contentItemRepository.findById(id);
    }

    /**
     * Save or update a content item.
     *
     * @param itemDTO the content item data
     * @return the saved content item
     */
    @Transactional
    public ContentItem saveContentItem(ContentItemDTO itemDTO) {
        log.debug("Saving content item: {}", itemDTO);
        
        ContentItem item = mapToEntity(itemDTO);
        item.setLastUpdated(OffsetDateTime.now());
        
        return contentItemRepository.save(item);
    }

    /**
     * Find similar content items based on genre overlap.
     *
     * @param itemId the reference item ID
     * @param limit the maximum number of items to return
     * @return a list of similar content items
     */
    @Transactional(readOnly = true)
    public List<ContentItem> findSimilarContent(String itemId, int limit) {
        return contentItemRepository.findSimilarContentByGenres(itemId, 1, limit);
    }

    /**
     * Find content items by genres.
     *
     * @param genres the genres
     * @return a list of content items
     */
    @Transactional(readOnly = true)
    public List<ContentItem> findByGenres(Set<String> genres) {
        return contentItemRepository.findByGenresIn(genres);
    }

    /**
     * Find the most popular content items.
     *
     * @param limit the maximum number of items to return
     * @return a list of popular content items
     */
    @Transactional(readOnly = true)
    public List<ContentItem> findMostPopular(int limit) {
        return contentItemRepository.findByOrderByPopularityScoreDesc(limit);
    }

    /**
     * Map DTO to entity.
     *
     * @param dto the DTO
     * @return the entity
     */
    private ContentItem mapToEntity(ContentItemDTO dto) {
        ContentItem item = contentItemRepository.findById(dto.getId())
                .orElse(new ContentItem());
        
        item.setId(dto.getId());
        item.setTitle(dto.getTitle());
        item.setItemType(dto.getItemType());
        item.setReleaseYear(dto.getReleaseYear());
        item.setDirector(dto.getDirector());
        item.setDuration(dto.getDuration());
        item.setMaturityRating(dto.getMaturityRating());
        item.setAverageRating(dto.getAverageRating());
        
        if (dto.getGenres() != null) {
            item.setGenres(dto.getGenres());
        }
        
        if (dto.getActors() != null) {
            item.setActors(dto.getActors());
        }
        
        if (dto.getKeywords() != null) {
            item.setKeywords(dto.getKeywords());
        }
        
        return item;
    }
}
