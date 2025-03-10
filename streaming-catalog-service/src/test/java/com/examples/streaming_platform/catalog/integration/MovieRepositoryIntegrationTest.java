package com.examples.streaming_platform.catalog.integration;

import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MovieRepositoryIntegrationTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void findByTitleContainingIgnoreCase_ShouldReturnMatchingMovies() {
        // Arrange
        Movie saved = new Movie();
        saved.setTitle("Hello World Movie");
        movieRepository.save(saved);

        // Act
        List<Movie> results = movieRepository.findByTitleContainingIgnoreCase("hello", null).getContent();

        // Assert
        assertFalse(results.isEmpty());
        assertEquals("Hello World Movie", results.get(0).getTitle());
    }
}
