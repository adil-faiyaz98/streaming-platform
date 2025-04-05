package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.ExternalMovieInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExternalApiServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private ExternalApiService externalApiService;

    @BeforeEach
    void setUp() {
        externalApiService = new ExternalApiService(restTemplate);
        ReflectionTestUtils.setField(externalApiService, "movieInfoApiUrl", "https://api.example.com/movies");
    }

    @Test
    void getMovieInfo_Success() throws ExecutionException, InterruptedException {
        // Given
        String movieId = "123";
        ExternalMovieInfoDTO expectedInfo = new ExternalMovieInfoDTO();
        expectedInfo.setId(movieId);
        expectedInfo.setTitle("Test Movie");
        
        when(restTemplate.getForObject(anyString(), eq(ExternalMovieInfoDTO.class)))
                .thenReturn(expectedInfo);

        // When
        CompletableFuture<ExternalMovieInfoDTO> future = externalApiService.getMovieInfo(movieId);
        ExternalMovieInfoDTO result = future.get();

        // Then
        assertNotNull(result);
        assertEquals(movieId, result.getId());
        assertEquals("Test Movie", result.getTitle());
    }

    @Test
    void getMovieInfo_WithException_ShouldReturnFallback() throws ExecutionException, InterruptedException {
        // Given
        String movieId = "123";
        
        when(restTemplate.getForObject(anyString(), eq(ExternalMovieInfoDTO.class)))
                .thenThrow(new RuntimeException("API Error"));

        // When
        CompletableFuture<ExternalMovieInfoDTO> future = externalApiService.getMovieInfo(movieId);
        ExternalMovieInfoDTO result = future.get();

        // Then
        assertNotNull(result);
        assertEquals(movieId, result.getId());
        assertEquals("Unavailable", result.getTitle());
        assertEquals("Movie information temporarily unavailable", result.getDescription());
    }
}
