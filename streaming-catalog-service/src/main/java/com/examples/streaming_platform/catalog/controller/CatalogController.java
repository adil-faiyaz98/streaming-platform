package main.java.com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.EpisodeDTO;
import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.dto.SeasonDTO;
import com.examples.streaming_platform.catalog.dto.SeriesDTO;
import com.examples.streaming_platform.catalog.dto.TvShowDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Catalog API", description = "Operations for managing streaming content catalog")
public class CatalogController {

    private final CatalogService catalogService;

    // ----- Movie Endpoints -----

    @GetMapping("/movies")
    @Operation(summary = "Get all movies", description = "Returns a paginated list of movies")
    public ResponseEntity<Page<MovieDTO>> getAllMovies(
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        return ResponseEntity.ok(catalogService.getAllMovies(pageable));
    }

    @GetMapping("/movies/{id}")
    @Operation(summary = "Get movie by ID", description = "Returns a single movie by its ID")
    public ResponseEntity<MovieDTO> getMovieById(
            @Parameter(description = "Movie ID", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getMovieById(id));
    }

    @GetMapping("/movies/search")
    @Operation(summary = "Search movies by title", description = "Returns movies containing the given title")
    public ResponseEntity<Page<MovieDTO>> searchMoviesByTitle(
            @Parameter(description = "Movie title to search for", required = true)
            @RequestParam String title,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(catalogService.searchMoviesByTitle(title, pageable));
    }

    @GetMapping("/movies/genre/{genre}")
    @Operation(summary = "Get movies by genre", description = "Returns movies with the specified genre")
    public ResponseEntity<Page<MovieDTO>> getMoviesByGenre(
            @Parameter(description = "Genre name", required = true)
            @PathVariable String genre,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(catalogService.getMoviesByGenre(genre, pageable));
    }

    @GetMapping("/movies/top-rated")
    @Operation(summary = "Get top rated movies", description = "Returns the top rated movies")
    public ResponseEntity<List<MovieDTO>> getTopRatedMovies() {
        return ResponseEntity.ok(catalogService.getTopRatedMovies());
    }

    @GetMapping("/movies/featured")
    @Operation(summary = "Get featured movies", description = "Returns the featured movies")
    public ResponseEntity<List<MovieDTO>> getFeaturedMovies() {
        return ResponseEntity.ok(catalogService.getFeaturedMovies());
    }

    @PostMapping("/movies")
    @Operation(summary = "Create a new movie", description = "Creates a new movie entry in the catalog")
    @ApiResponse(responseCode = "201", description = "Movie created successfully")
    public ResponseEntity<MovieDTO> createMovie(
            @Parameter(description = "Movie details", required = true)
            @Valid @RequestBody MovieDTO movieDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.createMovie(movieDTO));
    }

    @PutMapping("/movies/{id}")
    @Operation(summary = "Update a movie", description = "Updates an existing movie in the catalog")
    public ResponseEntity<MovieDTO> updateMovie(
            @Parameter(description = "Movie ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated movie details", required = true)
            @Valid @RequestBody MovieDTO movieDTO) {
        return ResponseEntity.ok(catalogService.updateMovie(id, movieDTO));
    }

    @DeleteMapping("/movies/{id}")
    @Operation(summary = "Delete a movie", description = "Deletes a movie from the catalog")
    @ApiResponse(responseCode = "204", description = "Movie deleted successfully")
    public ResponseEntity<Void> deleteMovie(
            @Parameter(description = "Movie ID", required = true)
            @PathVariable Long id) {
        catalogService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/movies/{id}/increment-view")
    @Operation(summary = "Increment movie view count", description = "Increments the view count for a movie")
    public ResponseEntity<Void> incrementMovieViewCount(
            @Parameter(description = "Movie ID", required = true)
            @PathVariable Long id) {
        catalogService.incrementMovieViewCount(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/movies/genre-stats")
    @Operation(summary = "Get movie genre statistics", description = "Returns statistics about movie genres")
    public ResponseEntity<Map<String, Long>> getMovieGenreStats() {
        return ResponseEntity.ok(catalogService.getMovieGenreStats());
    }

    // ----- Series Endpoints -----

    @GetMapping("/series")
    @Operation(summary = "Get all series", description = "Returns a paginated list of TV series")
    public ResponseEntity<Page<SeriesDTO>> getAllSeries(
            @PageableDefault(size = 10, sort = "title") Pageable pageable) {
        return ResponseEntity.ok(catalogService.getAllSeries(pageable));
    }

    @GetMapping("/series/{id}")
    @Operation(summary = "Get series by ID", description = "Returns a single TV series by its ID")
    public ResponseEntity<SeriesDTO> getSeriesById(
            @Parameter(description = "Series ID", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getSeriesById(id));
    }

    @GetMapping("/series/search")
    @Operation(summary = "Search series by title", description = "Returns TV series containing the given title")
    public ResponseEntity<Page<SeriesDTO>> searchSeriesByTitle(
            @Parameter(description = "Series title to search for", required = true)
            @RequestParam String title,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(catalogService.searchSeriesByTitle(title, pageable));
    }

    @GetMapping("/series/genre/{genre}")
    @Operation(summary = "Get series by genre", description = "Returns TV series with the specified genre")
    public ResponseEntity<Page<SeriesDTO>> getSeriesByGenre(
            @Parameter(description = "Genre name", required = true)
            @PathVariable String genre,
            @PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.ok(catalogService.getSeriesByGenre(genre, pageable));
    }

    @GetMapping("/series/top-rated")
    @Operation(summary = "Get top rated series", description = "Returns the top rated TV series")
    public ResponseEntity<List<SeriesDTO>> getTopRatedSeries() {
        return ResponseEntity.ok(catalogService.getTopRatedSeries());
    }

    @GetMapping("/series/featured")
    @Operation(summary = "Get featured series", description = "Returns the featured TV series")
    public ResponseEntity<List<SeriesDTO>> getFeaturedSeries() {
        return ResponseEntity.ok(catalogService.getFeaturedSeries());
    }

    @PostMapping("/series")
    @Operation(summary = "Create a new series", description = "Creates a new TV series entry in the catalog")
    @ApiResponse(responseCode = "201", description = "Series created successfully")
    public ResponseEntity<SeriesDTO> createSeries(
            @Parameter(description = "Series details", required = true)
            @Valid @RequestBody SeriesDTO seriesDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(catalogService.createSeries(seriesDTO));
    }

    @PutMapping("/series/{id}")
    @Operation(summary = "Update a series", description = "Updates an existing TV series in the catalog")
    public ResponseEntity<SeriesDTO> updateSeries(
            @Parameter(description = "Series ID", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated series details", required = true)
            @Valid @RequestBody SeriesDTO seriesDTO) {
        return ResponseEntity.ok(catalogService.updateSeries(id, seriesDTO));
    }

    @DeleteMapping("/series/{id}")
    @Operation(summary = "Delete a series", description = "Deletes a TV series from the catalog")
    @ApiResponse(responseCode = "204", description = "Series deleted successfully")
    public ResponseEntity<Void> deleteSeries(
            @Parameter(description = "Series ID", required = true)
            @PathVariable Long id) {
        catalogService.deleteSeries(id);
        return ResponseEntity.noContent().build();
    }

    // TV Show endpoints
    @GetMapping("/tvshows")
    @Operation(summary = "Get all TV shows", description = "Returns a paginated list of all TV shows")
    public ResponseEntity<Page<TvShowDTO>> getAllTvShows(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(catalogService.getAllTvShows(pageable));
    }

    @GetMapping("/tvshows/{id}")
    @Operation(summary = "Get TV show by ID", description = "Returns a single TV show by its ID")
    public ResponseEntity<TvShowDTO> getTvShowById(@PathVariable Long id) {
        return ResponseEntity.ok(catalogService.getTvShowById(id));
    }

    @PostMapping("/tvshows")
    @Operation(summary = "Create a new TV show", description = "Adds a new TV show to the catalog")
    public ResponseEntity<TvShowDTO> createTvShow(@Valid @RequestBody TvShowDTO tvShowDTO) {
        return new ResponseEntity<>(catalogService.createTvShow(tvShowDTO), HttpStatus.CREATED);
    }

    @PutMapping("/tvshows/{id}")
    @Operation(summary = "Update a TV show", description = "Updates an existing TV show in the catalog")
    public ResponseEntity<TvShowDTO> updateTvShow(@PathVariable Long id, @Valid @RequestBody TvShowDTO tvShowDTO) {
        return ResponseEntity.ok(catalogService.updateTvShow(id, tvShowDTO));
    }

    @DeleteMapping("/tvshows/{id}")
    @Operation(summary = "Delete a TV show", description = "Removes a TV show from the catalog")
    public ResponseEntity<Void> deleteTvShow(@PathVariable Long id) {
        catalogService.deleteTvShow(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tvshows/search")
    @Operation(summary = "Search TV shows by title", description = "Returns TV shows matching the search term in their title")
    public ResponseEntity<Page<TvShowDTO>> searchTvShowsByTitle(
            @RequestParam String title,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(catalogService.searchTvShowsByTitle(title, pageable));
    }

    @GetMapping("/tvshows/filter")
    @Operation(summary = "Filter TV shows by genres", description = "Returns TV shows that match the specified genres")
    public ResponseEntity<Page<TvShowDTO>> filterTvShowsByGenres(
            @RequestParam Set<String> genres,
            @PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(catalogService.searchTvShowsByGenres(genres, pageable));
    }
}