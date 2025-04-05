package com.examples.streaming_platform.recommendation.controller;

import com.examples.streaming_platform.recommendation.dto.UserInteractionDTO;
import com.examples.streaming_platform.recommendation.model.UserInteraction;
import com.examples.streaming_platform.recommendation.service.UserInteractionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for user interaction endpoints.
 */
@RestController
@RequestMapping("/api/v1/interactions")
@RequiredArgsConstructor
@Tag(name = "User Interactions", description = "API for recording user interactions with content")
@Slf4j
public class UserInteractionController {

    private final UserInteractionService userInteractionService;

    /**
     * Record a user interaction.
     *
     * @param interactionDTO the interaction data
     * @return the recorded interaction
     */
    @PostMapping
    @Operation(summary = "Record a user interaction")
    public ResponseEntity<UserInteraction> recordInteraction(
            @Valid @RequestBody UserInteractionDTO interactionDTO) {
        
        log.debug("Recording interaction: {}", interactionDTO);
        UserInteraction interaction = userInteractionService.recordInteraction(interactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(interaction);
    }

    /**
     * Record a view interaction.
     *
     * @param userId the user ID
     * @param itemId the item ID
     * @param itemType the item type
     * @return the recorded interaction
     */
    @PostMapping("/view")
    @Operation(summary = "Record a view interaction")
    public ResponseEntity<UserInteraction> recordView(
            @RequestParam String userId,
            @RequestParam String itemId,
            @RequestParam UserInteraction.ItemType itemType) {
        
        log.debug("Recording view: user={}, item={}, type={}", userId, itemId, itemType);
        
        UserInteractionDTO interactionDTO = UserInteractionDTO.builder()
                .userId(userId)
                .itemId(itemId)
                .itemType(itemType)
                .interactionType(UserInteraction.InteractionType.VIEW)
                .build();
        
        UserInteraction interaction = userInteractionService.recordInteraction(interactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(interaction);
    }

    /**
     * Record a rating interaction.
     *
     * @param userId the user ID
     * @param itemId the item ID
     * @param itemType the item type
     * @param rating the rating value
     * @return the recorded interaction
     */
    @PostMapping("/rating")
    @Operation(summary = "Record a rating interaction")
    public ResponseEntity<UserInteraction> recordRating(
            @RequestParam String userId,
            @RequestParam String itemId,
            @RequestParam UserInteraction.ItemType itemType,
            @RequestParam Double rating) {
        
        log.debug("Recording rating: user={}, item={}, type={}, rating={}", 
                userId, itemId, itemType, rating);
        
        UserInteractionDTO interactionDTO = UserInteractionDTO.builder()
                .userId(userId)
                .itemId(itemId)
                .itemType(itemType)
                .interactionType(UserInteraction.InteractionType.RATING)
                .value(rating)
                .build();
        
        UserInteraction interaction = userInteractionService.recordInteraction(interactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(interaction);
    }

    /**
     * Record a watch time interaction.
     *
     * @param userId the user ID
     * @param itemId the item ID
     * @param itemType the item type
     * @param minutes the watch time in minutes
     * @return the recorded interaction
     */
    @PostMapping("/watch-time")
    @Operation(summary = "Record a watch time interaction")
    public ResponseEntity<UserInteraction> recordWatchTime(
            @RequestParam String userId,
            @RequestParam String itemId,
            @RequestParam UserInteraction.ItemType itemType,
            @RequestParam Double minutes) {
        
        log.debug("Recording watch time: user={}, item={}, type={}, minutes={}", 
                userId, itemId, itemType, minutes);
        
        UserInteractionDTO interactionDTO = UserInteractionDTO.builder()
                .userId(userId)
                .itemId(itemId)
                .itemType(itemType)
                .interactionType(UserInteraction.InteractionType.WATCH_TIME)
                .value(minutes)
                .build();
        
        UserInteraction interaction = userInteractionService.recordInteraction(interactionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(interaction);
    }
}
