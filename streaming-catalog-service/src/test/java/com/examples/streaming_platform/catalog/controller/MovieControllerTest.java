package test.java.com.examples.streaming_platform.catalog.controller;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

    @Mock
    private MovieService movieService;

    @InjectMocks
    private MovieController movieController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private MovieDTO movieDTO;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules(); // To handle Java 8 date/time types

        mockMvc = MockMvcBuilders.standaloneSetup(movieController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();

        movieDTO = new MovieDTO();
        movieDTO.setId(1L);
        movieDTO.setTitle("Test Movie");
        movieDTO.setDescription("Test Description");
        movieDTO.setReleaseDate(LocalDate.of(2023, 1, 1));
        movieDTO.setDuration(120);
        movieDTO.setGenres(new HashSet<>(Collections.singletonList("Action")));
        movieDTO.setRating(4.5);
    }

    @Test
    void getAllMovies_ShouldReturnMovies() throws Exception {
        Page<MovieDTO> page = new PageImpl<>(Collections.singletonList(movieDTO));
        when(movieService.getAllMovies(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/movies"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].title", is("Test Movie")));

        verify(movieService).getAllMovies(any(Pageable.class));
    }

    @Test
    void getMovieById_ShouldReturnMovie_WhenExists() throws Exception {
        when(movieService.getMovieById(1L)).thenReturn(movieDTO);

        mockMvc.perform(get("/api/v1/movies/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Movie")));

        verify(movieService).getMovieById(1L);
    }

    @Test
    void getMovieById_ShouldReturn404_WhenMovieDoesNotExist() throws Exception {
        when(movieService.getMovieById(999L)).thenThrow(new ResourceNotFoundException("Movie", "id", 999L));

        mockMvc.perform(get("/api/v1/movies/{id}", 999))
                .andExpect(status().isNotFound());

        verify(movieService).getMovieById(999L);
    }

    @Test
    void searchMovies_ShouldReturnMatchingMovies() throws Exception {
        Page<MovieDTO> page = new PageImpl<>(Collections.singletonList(movieDTO));
        when(movieService.searchMoviesByTitle(eq("Test"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/movies/search")
                        .param("title", "Test"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is("Test Movie")));

        verify(movieService).searchMoviesByTitle(eq("Test"), any(Pageable.class));
    }

    @Test
    void getMoviesByGenre_ShouldReturnMoviesInGenre() throws Exception {
        Page<MovieDTO> page = new PageImpl<>(Collections.singletonList(movieDTO));
        when(movieService.getMoviesByGenre(eq("Action"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/movies/genre/{genre}", "Action"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].title", is("Test Movie")));

        verify(movieService).getMoviesByGenre(eq("Action"), any(Pageable.class));
    }

    @Test
    void getTopRatedMovies_ShouldReturnTopMovies() throws Exception {
        List<MovieDTO> movies = Collections.singletonList(movieDTO);
        when(movieService.getTopRatedMovies()).thenReturn(movies);

        mockMvc.perform(get("/api/v1/movies/top-rated"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title", is("Test Movie")));

        verify(movieService).getTopRatedMovies();
    }

    @Test
    void createMovie_ShouldCreateAndReturnMovie() throws Exception {
        when(movieService.createMovie(any(MovieDTO.class))).thenReturn(movieDTO);

        mockMvc.perform(post("/api/v1/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Movie")));

        verify(movieService).createMovie(any(MovieDTO.class));
    }

    @Test
    void updateMovie_ShouldUpdateAndReturnMovie() throws Exception {
        when(movieService.updateMovie(eq(1L), any(MovieDTO.class))).thenReturn(movieDTO);

        mockMvc.perform(put("/api/v1/movies/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(movieDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.title", is("Test Movie")));

        verify(movieService).updateMovie(eq(1L), any(MovieDTO.class));
    }

    @Test
    void deleteMovie_ShouldReturnNoContent() throws Exception {
        doNothing().when(movieService).deleteMovie(1L);

        mockMvc.perform(delete("/api/v1/movies/{id}", 1))
                .andExpect(status().isNoContent());

        verify(movieService).deleteMovie(1L);
    }
}