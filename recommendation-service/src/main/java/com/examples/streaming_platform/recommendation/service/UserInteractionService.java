package com.examples.streaming_platform.recommendation.service;

import com.examples.streaming_platform.recommendation.dto.UserInteractionDTO;
import com.examples.streaming_platform.recommendation.model.UserInteraction;
import com.examples.streaming_platform.recommendation.repository.UserInteractionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

/**
 * Service for handling user interactions.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserInteractionService {

    private final UserInteractionRepository userInteractionRepository;
    private final ObjectMapper objectMapper;

    /**
     * Record a user interaction.
     *
     * @param interactionDTO the interaction data
     * @return the saved interaction
     */
    @Transactional
    public UserInteraction recordInteraction(UserInteractionDTO interactionDTO) {
        log.debug("Recording interaction: {}", interactionDTO);
        
        UserInteraction interaction = mapToEntity(interactionDTO);
        
        // Set timestamp if not provided
        if (interaction.getTimestamp() == null) {
            interaction.setTimestamp(OffsetDateTime.now());
        }
        
        // Convert context data to JSON string if provided
        if (interactionDTO.getContextData() != null) {
            try {
                interaction.setContextData(objectMapper.writeValueAsString(interactionDTO.getContextData()));
            } catch (Exception e) {
                log.error("Error serializing context data", e);
            }
        }
        
        return userInteractionRepository.save(interaction);
    }

    /**
     * Get the most recent interaction between a user and an item.
     *
     * @param userId the user ID
     * @param itemId the item ID
     * @return the most recent interaction, if any
     */
    @Transactional(readOnly = true)
    public UserInteraction getMostRecentInteraction(String userId, String itemId) {
        return userInteractionRepository
                .findTopByUserIdAndItemIdOrderByTimestampDesc(userId, itemId)
                .orElse(null);
    }

    /**
     * Map DTO to entity.
     *
     * @param dto the DTO
     * @return the entity
     */
    private UserInteraction mapToEntity(UserInteractionDTO dto) {
        return UserInteraction.builder()
                .userId(dto.getUserId())
                .itemId(dto.getItemId())
                .itemType(dto.getItemType())
                .interactionType(dto.getInteractionType())
                .value(dto.getValue())
                .timestamp(dto.getTimestamp())
                .build();
    }
}
