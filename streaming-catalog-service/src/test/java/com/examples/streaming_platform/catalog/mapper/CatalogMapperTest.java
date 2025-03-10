package com.examples.streaming_platform.catalog.mapper;

import com.examples.streaming_platform.catalog.dto.MovieDTO;
import com.examples.streaming_platform.catalog.model.Movie;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class CatalogMapperTest {

    private final CatalogMapper mapper = Mappers.getMapper(CatalogMapper.class);

    @Test
    void movieToMovieDTO_ShouldMapFields() {
        // Given
        Movie movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Test Movie");
        movie.setDescription("Description");

        // When
        MovieDTO dto = mapper.movieToMovieDTO(movie);

        // Then
        assertEquals(1L, dto.getId());
        assertEquals("Test Movie", dto.getTitle());
        assertEquals("Description", dto.getDescription());
    }
}
