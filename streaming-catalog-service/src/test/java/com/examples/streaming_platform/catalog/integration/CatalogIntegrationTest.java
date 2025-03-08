// src/test/java/com/examples/streaming_platform/catalog/integration/CatalogIntegrationTest.java

package com.examples.streaming_platform.catalog.integration;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.repository.MovieRepository;
import com.examples.streaming_platform.catalog.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
class CatalogIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void shouldCreateAndRetrieveMovie() {
        // Given
        MovieDTO movieDTO = new MovieDTO();
        movieDTO.setTitle("Integration Test Movie");
        movieDTO.setDescription("Test Description");
        movieDTO.setReleaseYear(2024);
        movieDTO.setGenres(Set.of("Action", "Drama"));
        movieDTO.setDirector("Test Director");

        // When
        MovieDTO createdMovie = catalogService.createMovie(movieDTO);

        // Then
        assertNotNull(createdMovie.getId());
        MovieDTO retrievedMovie = catalogService.getMovieById(createdMovie.getId());
        assertEquals(createdMovie.getTitle(), retrievedMovie.getTitle());
        assertEquals(createdMovie.getDescription(), retrievedMovie.getDescription());
        assertEquals(createdMovie.getReleaseYear(), retrievedMovie.getReleaseYear());
        assertEquals(createdMovie.getGenres(), retrievedMovie.getGenres());
        assertEquals(createdMovie.getDirector(), retrievedMovie.getDirector());
    }
}