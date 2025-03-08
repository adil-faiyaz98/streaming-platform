package main.java.com.examples.streaming_platform.catalog.graphql;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class MovieResolver {

    private final MovieService movieService;

    @QueryMapping
    public Page<MovieDTO> movies(@Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page, size);
        return movieService.getAllMovies(pageable);
    }

    @QueryMapping
    public MovieDTO movie(@Argument String id) {
        return movieService.getMovieById(Long.valueOf(id));
    }

    @QueryMapping
    public Page<MovieDTO> searchMovies(@Argument String title, @Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page, size);
        return movieService.searchMoviesByTitle(title, pageable);
    }

    @QueryMapping
    public Page<MovieDTO> moviesByGenre(@Argument String genre, @Argument int page, @Argument int size) {
        Pageable pageable = PageRequest.of(page, size);
        return movieService.getMoviesByGenre(genre, pageable);
    }

    @QueryMapping
    public List<MovieDTO> topRatedMovies() {
        return movieService.getTopRatedMovies();
    }

    @QueryMapping
    public List<MovieDTO> featuredMovies() {
        return movieService.getFeaturedMovies();
    }

    @QueryMapping
    public List<Map<String, Object>> movieGenreStats() {
        return movieService.getGenreStatistics();
    }

    @MutationMapping
    public MovieDTO createMovie(@Argument("input") MovieDTO movieInput) {
        return movieService.createMovie(movieInput);
    }

    @MutationMapping
    public MovieDTO updateMovie(@Argument String id, @Argument("input") MovieDTO movieInput) {
        return movieService.updateMovie(Long.valueOf(id), movieInput);
    }

    @MutationMapping
    public Boolean deleteMovie(@Argument String id) {
        movieService.deleteMovie(Long.valueOf(id));
        return true;
    }
}