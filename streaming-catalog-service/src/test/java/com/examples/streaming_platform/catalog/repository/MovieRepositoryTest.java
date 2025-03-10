package com.examples.streaming_platform.catalog.repository;

import com.examples.streaming_platform.catalog.model.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

@DataJpaTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Test
    void findTop10ByOrderByRatingDesc_ShouldReturnMax10() {
        // Insert some test movies
        for (int i = 1; i <= 12; i++) {
            Movie m = new Movie();
            m.setTitle("Movie " + i);
            m.setRating(i);
            movieRepository.save(m);
        }

        List<Movie> topMovies = movieRepository.findTop10ByOrderByRatingDesc();
        assertEquals(10, topMovies.size());
        assertTrue(topMovies.get(0).getRating() >= topMovies.get(9).getRating());
    }
}
