package test.java.com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.repository.MovieRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private MovieRepository movieRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Movie testMovie;
    
    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
        
        testMovie = new Movie();
        testMovie.setTitle("Integration Test Movie");
        testMovie.setDescription("Test Description");
        testMovie.setReleaseDate(LocalDate.of(2023, 1, 1));
        testMovie.setDuration(120);
        
        Set<String> genres = new HashSet<>();
        genres.add("Action");
        genres.add("Sci-Fi");
        testMovie.setGenres(genres);
        
        testMovie.setRating(4.5);
        testMovie = movieRepository.save(testMovie);
    }
    
    @AfterEach
    void tearDown() {
        movieRepository.deleteAll();
    }
    
    @Test
    void getAllMovies_ShouldReturnMovies() throws Exception {
        mockMvc.perform(get("/api/v1/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.content[*].title", hasItem("Integration Test Movie")));
    }
    
    @Test
    void getMovieById_ShouldReturnMovie_WhenExists() throws Exception {
        mockMvc.perform(get("/api/v1/movies/{id}", testMovie.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testMovie.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Integration Test Movie")));
    }
    
    @Test
    void getMovieById_ShouldReturn404_WhenDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/v1/movies/{id}", 999))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void createMovie_ShouldSaveAndReturnMovie() throws Exception {
        MovieDTO newMovie = new MovieDTO();
        newMovie.setTitle("New Test Movie");
        newMovie.setDescription("New Description");
        newMovie.setReleaseDate(LocalDate.of(2022, 1, 1));
        newMovie.setDuration(130);
        newMovie.setRating(4.0);
        
        Set<String> genres = new HashSet<>();
        genres.add("Comedy");
        newMovie.setGenres(genres);
        
        mockMvc.perform(post("/api/v1/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newMovie)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title", is("New Test Movie")))
                .andExpect(jsonPath("$.id", notNullValue()));
    }
    
    @Test
    void updateMovie_ShouldUpdateAndReturnMovie() throws Exception {
        MovieDTO updateDTO = new MovieDTO();
        updateDTO.setTitle("Updated Title");
        updateDTO.setDescription("Updated Description");
        
        mockMvc.perform(put("/api/v1/movies/{id}", testMovie.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testMovie.getId().intValue())))
                .andExpect(jsonPath("$.title", is("Updated Title")))
                .andExpect(jsonPath("$.description", is("Updated Description")));
    }
    
    @Test
    void deleteMovie_ShouldRemoveMovie() throws Exception {
        mockMvc.perform(delete("/api/v1/movies/{id}", testMovie.getId()))
                .andExpect(status().isNoContent());
        
        // Verify the movie is no longer available
        mockMvc.perform(get("/api/v1/movies/{id}", testMovie.getId()))
                .andExpect(status().isNotFound());
    }
}