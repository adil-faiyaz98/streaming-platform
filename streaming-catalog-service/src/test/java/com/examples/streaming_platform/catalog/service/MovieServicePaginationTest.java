package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Sql("/test-data/movies.sql")
class MovieServicePaginationTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void findAll_WithPagination_ShouldReturnCorrectPage() {
        // Given
        Pageable pageable = PageRequest.of(0, 5, Sort.by("title").ascending());

        // When
        Page<Movie> result = movieRepository.findAll(pageable);

        // Then
        assertEquals(5, result.getContent().size());
        assertEquals(0, result.getNumber());
        assertTrue(result.getTotalElements() >= 5);
    }

    @Test
    void findAll_WithPaginationAndSorting_ShouldReturnSortedResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("releaseYear").descending());

        // When
        Page<Movie> result = movieRepository.findAll(pageable);

        // Then
        assertTrue(result.getContent().size() > 0);
        
        // Verify sorting
        for (int i = 0; i < result.getContent().size() - 1; i++) {
            Movie current = result.getContent().get(i);
            Movie next = result.getContent().get(i + 1);
            
            // If release years are available, verify descending order
            if (current.getReleaseYear() != null && next.getReleaseYear() != null) {
                assertTrue(current.getReleaseYear() >= next.getReleaseYear(),
                        "Movies should be sorted by release year in descending order");
            }
        }
    }

    @Test
    void findByTitleContaining_WithPagination_ShouldReturnMatchingResults() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        String searchTerm = "a"; // Common letter that should match multiple titles

        // When
        Page<Movie> result = movieRepository.findByTitleContainingIgnoreCase(searchTerm, pageable);

        // Then
        assertTrue(result.getContent().size() > 0);
        
        // Verify all results contain the search term
        for (Movie movie : result.getContent()) {
            assertTrue(movie.getTitle().toLowerCase().contains(searchTerm.toLowerCase()),
                    "All results should contain the search term");
        }
    }
}
