package com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.graphql.controller.MovieGraphQLController;
import com.examples.streaming_platform.catalog.service.CatalogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@GraphQlTest(MovieGraphQLController.class)
class MovieGraphQLControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private CatalogService catalogService;

    @Test
    @WithMockUser(authorities = "SCOPE_read:movies")
    void getMovie_ShouldReturnMovie() {
        // Given
        MovieDTO movie = new MovieDTO();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setReleaseYear(2020);
        
        when(catalogService.getMovieById(1L)).thenReturn(movie);

        // When & Then
        String query = """
                query {
                  movie(id: 1) {
                    id
                    title
                    releaseYear
                  }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("movie")
                .entity(MovieDTO.class)
                .isEqualTo(movie);
    }

    @Test
    @WithMockUser(authorities = "SCOPE_read:movies")
    void getMovies_ShouldReturnMoviesList() {
        // Given
        MovieDTO movie1 = new MovieDTO();
        movie1.setId(1L);
        movie1.setTitle("Movie 1");
        
        MovieDTO movie2 = new MovieDTO();
        movie2.setId(2L);
        movie2.setTitle("Movie 2");
        
        List<MovieDTO> movies = Arrays.asList(movie1, movie2);
        
        when(catalogService.getAllMovies(any(PageRequest.class)))
                .thenReturn(new PageImpl<>(movies));

        // When & Then
        String query = """
                query {
                  movies {
                    id
                    title
                  }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("movies")
                .entityList(MovieDTO.class)
                .hasSize(2)
                .contains(movie1, movie2);
    }

    @Test
    @WithMockUser(authorities = "SCOPE_read:movies")
    void searchMovies_ShouldReturnMatchingMovies() {
        // Given
        MovieDTO movie = new MovieDTO();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        
        when(catalogService.searchMoviesByTitle(eq("Test"), any(PageRequest.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(movie)));

        // When & Then
        String query = """
                query {
                  searchMovies(title: "Test") {
                    id
                    title
                  }
                }
                """;

        graphQlTester.document(query)
                .execute()
                .path("searchMovies")
                .entityList(MovieDTO.class)
                .hasSize(1)
                .contains(movie);
    }
}
