package com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.service.CatalogService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CatalogController.class)
class CatalogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CatalogService catalogService;

    @Test
    @WithMockUser(authorities = "SCOPE_read:movies")
    void getAllMovies_ShouldReturnMoviesList() throws Exception {
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
        mockMvc.perform(get("/api/v1/catalog/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("Movie 1")))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].title", is("Movie 2")));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_read:movies")
    void getMovieById_ShouldReturnMovie() throws Exception {
        // Given
        MovieDTO movie = new MovieDTO();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setReleaseYear(2020);
        
        when(catalogService.getMovieById(1L)).thenReturn(movie);

        // When & Then
        mockMvc.perform(get("/api/v1/catalog/movies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Movie")))
                .andExpect(jsonPath("$.releaseYear", is(2020)));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_read:movies")
    void searchMoviesByTitle_ShouldReturnMatchingMovies() throws Exception {
        // Given
        MovieDTO movie = new MovieDTO();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        
        when(catalogService.searchMoviesByTitle(eq("Test"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(movie)));

        // When & Then
        mockMvc.perform(get("/api/v1/catalog/movies/search")
                .param("title", "Test"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("Test Movie")));
    }

    @Test
    @WithMockUser(authorities = "SCOPE_write:movies")
    void createMovie_ShouldReturnCreatedMovie() throws Exception {
        // Given
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("New Movie");
        movieDTO.setReleaseYear(2023);
        
        MovieDTO createdMovie = new MovieDTO();
        createdMovie.setId(1L);
        createdMovie.setTitle("New Movie");
        createdMovie.setReleaseYear(2023);
        
        when(catalogService.createMovie(any(MovieDTO.class))).thenReturn(createdMovie);

        // When & Then
        mockMvc.perform(post("/api/v1/catalog/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("New Movie")))
                .andExpect(jsonPath("$.releaseYear", is(2023)));
    }
}
