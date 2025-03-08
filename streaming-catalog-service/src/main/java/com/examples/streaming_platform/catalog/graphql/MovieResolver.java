package main.java.com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.service.MovieService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MovieResolver implements GraphQLQueryResolver, GraphQLMutationResolver {

    private final MovieService movieService;

    // Queries
    public MovieDTO getMovie(Long id) {
        return movieService.getMovieById(id);
    }

    public Map<String, Object> getMovies(Integer page, Integer size, Optional<String> title, Optional<String> genre) {
        PageRequest pageRequest = PageRequest.of(page != null ? page : 0, size != null ? size : 20);
        
        Page<MovieDTO> moviePage;
        if (title.isPresent() && !title.get().isEmpty()) {
            moviePage = movieService.searchMoviesByTitle(title.get(), pageRequest);
        } else if (genre.isPresent() && !genre.get().isEmpty()) {
            moviePage = movieService.getMoviesByGenre(genre.get(), pageRequest);
        } else {
            moviePage = movieService.getAllMovies(pageRequest);
        }
        
        return Map.of(
                "content", moviePage.getContent(),
                "totalElements", moviePage.getTotalElements(),
                "totalPages", moviePage.getTotalPages(),
                "size", moviePage.getSize(),
                "number", moviePage.getNumber()
        );
    }

    public List<MovieDTO> getTopRatedMovies() {
        return movieService.getTopRatedMovies();
    }

    // Mutations
    public MovieDTO createMovie(MovieDTO movieInput) {
        return movieService.createMovie(movieInput);
    }

    public MovieDTO updateMovie(Long id, MovieDTO movieInput) {
        return movieService.updateMovie(id, movieInput);
    }

    public Boolean deleteMovie(Long id) {
        try {
            movieService.deleteMovie(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}