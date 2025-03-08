package test.java.com.examples.streaming_platform.catalog.repository;

import com.examples.streaming_platform.catalog.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test")
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void findByTitleContainingIgnoreCase_ShouldReturnMatchingMovies() {
        // Arrange
        Movie movie1 = createMovie("The Test Movie", 4.0, "Action");
        Movie movie2 = createMovie("Another Movie", 3.5, "Comedy");
        Movie movie3 = createMovie("Test Film", 4.5, "Drama");
        
        movieRepository.saveAll(Arrays.asList(movie1, movie2, movie3));

        // Act
        Page<Movie> result = movieRepository.findByTitleContainingIgnoreCase("test", 
                PageRequest.of(0, 10));

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream()
                .anyMatch(m -> m.getTitle().equals("The Test Movie")));
        assertTrue(result.getContent().stream()
                .anyMatch(m -> m.getTitle().equals("Test Film")));
    }

    @Test
    void findByGenre_ShouldReturnMoviesWithSpecificGenre() {
        // Arrange
        Movie movie1 = createMovie("Movie 1", 4.0, "Action");
        Movie movie2 = createMovie("Movie 2", 3.5, "Comedy", "Action");
        Movie movie3 = createMovie("Movie 3", 4.5, "Drama");
        
        movieRepository.saveAll(Arrays.asList(movie1, movie2, movie3));

        // Act
        Page<Movie> result = movieRepository.findByGenre("Action", PageRequest.of(0, 10));

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream()
                .anyMatch(m -> m.getTitle().equals("Movie 1")));
        assertTrue(result.getContent().stream()
                .anyMatch(m -> m.getTitle().equals("Movie 2")));
    }
    
    @Test
    void findTop10ByOrderByRatingDesc_ShouldReturnTopRatedMovies() {
        // Arrange
        Movie movie1 = createMovie("Movie 1", 3.0, "Action");
        Movie movie2 = createMovie("Movie 2", 4.5, "Comedy");
        Movie movie3 = createMovie("Movie 3", 4.0, "Drama");
        Movie movie4 = createMovie("Movie 4", 5.0, "Thriller");
        
        movieRepository.saveAll(Arrays.asList(movie1, movie2, movie3, movie4));

        // Act
        List<Movie> result = movieRepository.findTop10ByOrderByRatingDesc();

        // Assert
        assertEquals(4, result.size());
        assertEquals("Movie 4", result.get(0).getTitle()); // Highest rating (5.0) should be first
        assertEquals("Movie 2", result.get(1).getTitle()); // Second highest (4.5)
        assertEquals("Movie 3", result.get(2).getTitle()); // Third highest (4.0)
        assertEquals("Movie 1", result.get(3).getTitle()); // Lowest rating (3.0) should be last
    }
    
    @Test
    void findAllReleaseYears_ShouldReturnDistinctYears() {
        // Arrange
        Movie movie1 = createMovie("Movie 1", 4.0, 2020, "Action");
        Movie movie2 = createMovie("Movie 2", 3.5, 2021, "Comedy");
        Movie movie3 = createMovie("Movie 3", 4.5, 2020, "Drama"); // Same year as movie1
        
        movieRepository.saveAll(Arrays.asList(movie1, movie2, movie3));

        // Act
        List<Integer> result = movieRepository.findAllReleaseYears();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(2020));
        assertTrue(result.contains(2021));
    }
    
    // Helper method to create movie entities for testing
    private Movie createMovie(String title, double rating, String... genres) {
        return createMovie(title, rating, 2023, genres); // Default year is 2023
    }
    
    private Movie createMovie(String title, double rating, int year, String... genres) {
        Movie movie = new Movie();
        movie.setTitle(title);
        movie.setDescription("Description for " + title);
        movie.setReleaseDate(LocalDate.of(year, 1, 1));
        movie.setDuration(120);
        movie.setPosterUrl("http://example.com/poster-" + title.toLowerCase().replace(' ', '-') + ".jpg");
        movie.setRating(rating);
        
        HashSet<String> genreSet = new HashSet<>();
        Collections.addAll(genreSet, genres);
        movie.setGenres(genreSet);
        
        return movie;
    }
}