package com.examples.streaming_platform.catalog.controller.v2;

import com.examples.streaming_platform.catalog.client.RecommendationClient;
import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.dto.MovieDetailedDTO;
import com.examples.streaming_platform.catalog.dto.RecommendationDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import com.examples.streaming_platform.catalog.service.ExternalApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * REST controller for movie operations (API v2).
 * This version includes enhanced features like detailed movie information and filtering.
 */
@RestController
@RequestMapping("/api/v2/movies")
@RequiredArgsConstructor
@Tag(name = "Movies API v2", description = "Enhanced operations for managing movies (API v2)")
@Slf4j
public class MovieControllerV2 {

    private final CatalogService catalogService;
    private final ExternalApiService externalApiService;
    private final RecommendationClient recommendationClient;

    /**
     * Get all movies with pagination and filtering.
     *
     * @param pageable pagination information
     * @param genre optional genre filter
     * @param releaseYear optional release year filter
     * @param minRating optional minimum rating filter
     * @return a page of movies
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    @Operation(summary = "Get all movies with filtering", description = "Returns a paginated and filtered list of movies")
    public ResponseEntity<Page<MovieDTO>> getAllMovies(
            @PageableDefault(size = 10, sort = "title") Pageable pageable,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer releaseYear,
            @RequestParam(required = false) Double minRating) {
        log.debug("V2 - Fetching all movies with filters: genre={}, releaseYear={}, minRating={}",
                genre, releaseYear, minRating);

        // In a real implementation, we would add these filters to the service
        // For now, we'll just call the existing method
        return ResponseEntity.ok(catalogService.getAllMovies(pageable));
    }

    /**
     * Get detailed movie information by ID.
     * This enhanced endpoint includes additional information from external APIs.
     *
     * @param id the movie ID
     * @return the detailed movie information
     */
    @GetMapping("/{id}/detailed")
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    @Operation(summary = "Get detailed movie information",
            description = "Returns detailed movie information including data from external sources")
    public CompletableFuture<ResponseEntity<MovieDetailedDTO>> getDetailedMovieById(@PathVariable Long id) {
        log.debug("V2 - Fetching detailed movie by ID: {}", id);

        // Get basic movie information
        MovieDTO movie = catalogService.getMovieById(id);

        // Get additional information from external API
        return externalApiService.getMovieInfo(id.toString())
                .thenApply(externalInfo -> {
                    // Create detailed DTO by combining internal and external data
                    MovieDetailedDTO detailedDTO = new MovieDetailedDTO(movie);

                    // Add external information if available
                    if (externalInfo != null) {
                        detailedDTO.setCast(externalInfo.getCast());
                        if (externalInfo.getRating() != null) {
                            detailedDTO.setExternalRating(externalInfo.getRating());
                        }
                        if (externalInfo.getPosterUrl() != null) {
                            detailedDTO.setPosterUrl(externalInfo.getPosterUrl());
                        }
                    }

                    return ResponseEntity.ok(detailedDTO);
                });
    }

    /**
     * Search movies with advanced filtering.
     *
     * @param query the search query
     * @param pageable pagination information
     * @param includeAdult whether to include adult content
     * @param sortBy sort field
     * @return a page of matching movies
     */
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    @Operation(summary = "Advanced movie search",
            description = "Search movies with advanced filtering options")
    public ResponseEntity<Page<MovieDTO>> searchMovies(
            @RequestParam String query,
            @PageableDefault(size = 10) Pageable pageable,
            @RequestParam(defaultValue = "false") boolean includeAdult,
            @RequestParam(defaultValue = "relevance") String sortBy) {
        log.debug("V2 - Advanced search: query={}, includeAdult={}, sortBy={}",
                query, includeAdult, sortBy);

        // In a real implementation, we would add these filters to the service
        // For now, we'll just call the existing method
        return ResponseEntity.ok(catalogService.searchMoviesByTitle(query, pageable));
    }

    /**
     * Create a new movie with enhanced validation.
     *
     * @param movieDTO the movie to create
     * @return the created movie
     */
    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_write:movies')")
    @Operation(summary = "Create a new movie",
            description = "Creates a new movie with enhanced validation")
    public ResponseEntity<MovieDTO> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        log.debug("V2 - Creating movie: {}", movieDTO);
        MovieDTO created = catalogService.createMovie(movieDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Get movie recommendations based on a movie ID.
     *
     * @param id the movie ID
     * @param limit the maximum number of recommendations
     * @param userId optional user ID for personalized recommendations
     * @return a list of recommended movies
     */
    @GetMapping("/{id}/recommendations")
    @PreAuthorize("hasAuthority('SCOPE_read:movies')")
    @Operation(summary = "Get AI-powered movie recommendations",
            description = "Returns AI-powered movie recommendations based on the given movie and user preferences")
    public CompletableFuture<ResponseEntity<List<MovieDTO>>> getRecommendations(
            @PathVariable Long id,
            @RequestParam(defaultValue = "5") int limit,
            @RequestParam(required = false) String userId) {

        log.debug("V2 - Getting AI recommendations for movie ID: {}, user: {}, limit: {}",
                id, userId, limit);

        // Get recommendations from AI service
        CompletableFuture<List<RecommendationDTO>> recommendationsFuture;

        if (userId != null) {
            // Personalized recommendations if user is logged in
            recommendationsFuture = recommendationClient.getRecommendationsForUser(userId, limit);
        } else {
            // Content-based recommendations if no user context
            recommendationsFuture = recommendationClient.getSimilarContent(id.toString(), limit);
        }

        // Convert recommendation DTOs to MovieDTOs
        return recommendationsFuture.thenApply(recommendations -> {
            // Extract item IDs from recommendations
            List<String> itemIds = recommendations.stream()
                    .map(RecommendationDTO::getItemId)
                    .toList();

            // If we got recommendations, fetch the full movie details
            if (!itemIds.isEmpty()) {
                return ResponseEntity.ok(catalogService.getMoviesByIds(itemIds));
            } else {
                // Fallback to top rated movies if no recommendations
                return ResponseEntity.ok(catalogService.getTopRatedMovies());
            }
        });
    }
}
