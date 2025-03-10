package com.examples.streaming_platform.catalog.exception;

import com.examples.streaming_platform.catalog.controller.MovieController;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import com.examples.streaming_platform.catalog.service.MovieService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MovieService movieService;

    @Test
    void handleNotFound_WhenEntityNotFound_ShouldReturn404() throws Exception {
        when(movieService.getMovieById(99L)).thenThrow(new EntityNotFoundException("Movie not found"));

        mockMvc.perform(get("/api/v1/movies/99"))
                .andExpect(status().isNotFound());
    }
}
