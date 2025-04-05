package com.examples.streaming_platform.recommendation.controller;

import com.examples.streaming_platform.recommendation.dto.ContentItemDTO;
import com.examples.streaming_platform.recommendation.model.ContentItem;
import com.examples.streaming_platform.recommendation.service.ContentItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for content item endpoints.
 */
@RestController
@RequestMapping("/api/v1/content-items")
@RequiredArgsConstructor
@Tag(name = "Content Items", description = "API for managing content items")
@Slf4j
public class ContentItemController {

    private final ContentItemService contentItemService;

    /**
     * Get a content item by ID.
     *
     * @param id the item ID
     * @return the content item
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a content item by ID")
    public ResponseEntity<ContentItem> getContentItem(@PathVariable String id) {
        log.debug("Getting content item: {}", id);
        
        Optional<ContentItem> contentItem = contentItemService.getContentItem(id);
        
        return contentItem
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Save or update a content item.
     *
     * @param itemDTO the content item data
     * @return the saved content item
     */
    @PostMapping
    @Operation(summary = "Save or update a content item")
    public ResponseEntity<ContentItem> saveContentItem(@Valid @RequestBody ContentItemDTO itemDTO) {
        log.debug("Saving content item: {}", itemDTO);
        
        ContentItem savedItem = contentItemService.saveContentItem(itemDTO);
        
        return ResponseEntity.status(HttpStatus.CREATED).body(savedItem);
    }
}
