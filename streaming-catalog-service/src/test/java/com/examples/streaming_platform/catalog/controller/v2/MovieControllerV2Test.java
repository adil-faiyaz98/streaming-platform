package com.examples.streaming_platform.catalog.controller.v2;

import com.examples.streaming_platform.catalog.dto.ExternalMovieInfoDTO;
import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
import com.examples.streaming_platform.catalog.service.ExternalApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieControllerV2.class)
class MovieControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CatalogService catalogService;

    @MockBean
    private ExternalApiService externalApiService;

    @Test
    @WithMockUser(authorities = "SCOPE_read:movies")
    void getAllMovies_WithFiltering_ShouldReturnMoviesList() throws Exception {
        // Given
        MovieDTO movie1 = new MovieDTO();
        movie1.setId(1L);
        movie1.setTitle("Movie 1");
        
        MovieDTO movie2 = new MovieDTO();
        movie2.setId(2L);
        movie2.setTitle("Movie 2");
        
        List<MovieDTO> movies = Arrays.asList(movie1, movie2);
        
        when(catalogService.getAllMovies(any(Pageable.class)))
                .thenReturn(new PageImpl<>(movies));

        // When & Then
        mockMvc.perform(get("/api/v2/movies")
                .param("genre", "ACTION")
                .param("releaseYear", "2023")
                .param("minRating", "4.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("Movie 1")))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].title", is("Movie 2")));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_read:movies")
    void getDetailedMovieById_ShouldReturnDetailedMovie() throws Exception {
        // Given
        Long movieId = 1L;
        
        MovieDTO movie = new MovieDTO();
        movie.setId(movieId);
        movie.setTitle("Test Movie");
        movie.setReleaseYear(2023);
        
        ExternalMovieInfoDTO externalInfo = new ExternalMovieInfoDTO();
        externalInfo.setId(movieId.toString());
        externalInfo.setTitle("Test Movie");
        externalInfo.setRating(4.8);
        externalInfo.setPosterUrl("https://example.com/poster.jpg");
        
        List<ExternalMovieInfoDTO.CastMemberDTO> cast = Collections.singletonList(
                new ExternalMovieInfoDTO.CastMemberDTO("Actor Name", "Character Name", "https://example.com/actor.jpg"));
        externalInfo.setCast(cast);
        
        when(catalogService.getMovieById(movieId)).thenReturn(movie);
        when(externalApiService.getMovieInfo(movieId.toString()))
                .thenReturn(CompletableFuture.completedFuture(externalInfo));

        // When & Then
        mockMvc.perform(get("/api/v2/movies/{id}/detailed", movieId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Movie")))
                .andExpect(jsonPath("$.releaseYear", is(2023)))
                .andExpect(jsonPath("$.externalRating", is(4.8)))
                .andExpect(jsonPath("$.posterUrl", is("https://example.com/poster.jpg")))
                .andExpect(jsonPath("$.cast", hasSize(1)))
                .andExpect(jsonPath("$.cast[0].name", is("Actor Name")))
                .andExpect(jsonPath("$.cast[0].character", is("Character Name")));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_read:movies")
    void searchMovies_WithAdvancedFiltering_ShouldReturnMatchingMovies() throws Exception {
        // Given
        MovieDTO movie = new MovieDTO();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        
        when(catalogService.searchMoviesByTitle(eq("test"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(movie)));

        // When & Then
        mockMvc.perform(get("/api/v2/movies/search")
                .param("query", "test")
                .param("includeAdult", "false")
                .param("sortBy", "relevance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("Test Movie")));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_read:movies")
    void getRecommendations_ShouldReturnRecommendedMovies() throws Exception {
        // Given
        Long movieId = 1L;
        
        MovieDTO movie1 = new MovieDTO();
        movie1.setId(2L);
        movie1.setTitle("Recommended Movie 1");
        
        MovieDTO movie2 = new MovieDTO();
        movie2.setId(3L);
        movie2.setTitle("Recommended Movie 2");
        
        List<MovieDTO> recommendations = Arrays.asList(movie1, movie2);
        
        when(catalogService.getTopRatedMovies()).thenReturn(recommendations);

        // When & Then
        mockMvc.perform(get("/api/v2/movies/{id}/recommendations", movieId)
                .param("limit", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].title", is("Recommended Movie 1")))
                .andExpect(jsonPath("$[1].id", is(3)))
                .andExpect(jsonPath("$[1].title", is("Recommended Movie 2")));
    }
}
