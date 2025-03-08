package com.examples.streaming_platform.catalog.service;

import com.examples.streaming_platform.catalog.dto.TvShowDTO;
import com.examples.streaming_platform.catalog.exception.ResourceNotFoundException;
import com.examples.streaming_platform.catalog.mapper.CatalogMapper;
import com.examples.streaming_platform.catalog.model.TvShow;
import com.examples.streaming_platform.catalog.repository.TvShowRepository;
import com.examples.streaming_platform.catalog.service.TvShowService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TvShowServiceTest {

    @Mock
    private TvShowRepository tvShowRepository;

    @Mock
    private CatalogMapper catalogMapper;

    @InjectMocks
    private TvShowService tvShowService;

    private TvShow tvShow;
    private TvShowDTO tvShowDTO;

    @BeforeEach
    void setUp() {
        tvShow = new TvShow();
        tvShow.setId(1L);
        tvShow.setTitle("Test TV Show");
        tvShow.setDescription("Test Description");
        tvShow.setFirstAirDate(LocalDate.of(2023, 1, 1));
        tvShow.setGenres(new HashSet<>(Collections.singletonList("Drama")));
        tvShow.setRating(4.5);

        tvShowDTO = new TvShowDTO();
        tvShowDTO.setId(1L);
        tvShowDTO.setTitle("Test TV Show");
        tvShowDTO.setDescription("Test Description");
        tvShowDTO.setFirstAirDate(LocalDate.of(2023, 1, 1));
        tvShowDTO.setGenres(new HashSet<>(Collections.singletonList("Drama")));
        tvShowDTO.setRating(4.5);
    }

    @Test
    void getAllTvShows_ShouldReturnPageOfTvShows() {
        Page<TvShow> tvShowPage = new PageImpl<>(Collections.singletonList(tvShow));

        when(tvShowRepository.findAll(any(Pageable.class))).thenReturn(tvShowPage);
        when(catalogMapper.tvShowToTvShowDTO(any(TvShow.class))).thenReturn(tvShowDTO);

        Page<TvShowDTO> result = tvShowService.getAllTvShows(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(tvShowDTO, result.getContent().get(0));

        verify(tvShowRepository).findAll(any(Pageable.class));
        verify(catalogMapper).tvShowToTvShowDTO(any(TvShow.class));
    }

    @Test
    void getTvShowById_ShouldReturnTvShow_WhenTvShowExists() {
        when(tvShowRepository.findById(1L)).thenReturn(Optional.of(tvShow));
        when(catalogMapper.tvShowToTvShowDTO(tvShow)).thenReturn(tvShowDTO);

        TvShowDTO result = tvShowService.getTvShowById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test TV Show", result.getTitle());

        verify(tvShowRepository).findById(1L);
        verify(catalogMapper).tvShowToTvShowDTO(tvShow);
    }

    @Test
    void getTvShowById_ShouldThrowException_WhenTvShowDoesNotExist() {
        when(tvShowRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tvShowService.getTvShowById(999L));

        verify(tvShowRepository).findById(999L);
        verifyNoInteractions(catalogMapper);
    }

    @Test
    void searchTvShowsByTitle_ShouldReturnMatchingTvShows() {
        Page<TvShow> tvShowPage = new PageImpl<>(Collections.singletonList(tvShow));

        when(tvShowRepository.findByTitleContainingIgnoreCase(eq("Test"), any(Pageable.class))).thenReturn(tvShowPage);
        when(catalogMapper.tvShowToTvShowDTO(any(TvShow.class))).thenReturn(tvShowDTO);

        Page<TvShowDTO> result = tvShowService.searchTvShowsByTitle("Test", Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(tvShowDTO, result.getContent().get(0));

        verify(tvShowRepository).findByTitleContainingIgnoreCase(eq("Test"), any(Pageable.class));
        verify(catalogMapper).tvShowToTvShowDTO(any(TvShow.class));
    }

    @Test
    void getTvShowsByGenre_ShouldReturnTvShowsInGenre() {
        Page<TvShow> tvShowPage = new PageImpl<>(Collections.singletonList(tvShow));

        when(tvShowRepository.findByGenre(eq("Drama"), any(Pageable.class))).thenReturn(tvShowPage);
        when(catalogMapper.tvShowToTvShowDTO(any(TvShow.class))).thenReturn(tvShowDTO);

        Page<TvShowDTO> result = tvShowService.getTvShowsByGenre("Drama", Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(tvShowDTO, result.getContent().get(0));

        verify(tvShowRepository).findByGenre(eq("Drama"), any(Pageable.class));
        verify(catalogMapper).tvShowToTvShowDTO(any(TvShow.class));
    }

    @Test
    void getTopRatedTvShows_ShouldReturnTopTenTvShows() {
        when(tvShowRepository.findTop10ByOrderByRatingDesc()).thenReturn(Collections.singletonList(tvShow));
        when(catalogMapper.tvShowToTvShowDTO(any(TvShow.class))).thenReturn(tvShowDTO);

        List<TvShowDTO> result = tvShowService.getTopRatedTvShows();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(tvShowDTO, result.get(0));

        verify(tvShowRepository).findTop10ByOrderByRatingDesc();
        verify(catalogMapper).tvShowToTvShowDTO(any(TvShow.class));
    }

    @Test
    void createTvShow_ShouldReturnCreatedTvShow() {
        when(catalogMapper.tvShowDTOToTvShow(tvShowDTO)).thenReturn(tvShow);
        when(tvShowRepository.save(tvShow)).thenReturn(tvShow);
        when(catalogMapper.tvShowToTvShowDTO(tvShow)).thenReturn(tvShowDTO);

        TvShowDTO result = tvShowService.createTvShow(tvShowDTO);

        assertNotNull(result);
        assertEquals(tvShowDTO.getTitle(), result.getTitle());

        verify(catalogMapper).tvShowDTOToTvShow(tvShowDTO);
        verify(tvShowRepository).save(tvShow);
        verify(catalogMapper).tvShowToTvShowDTO(tvShow);
    }

    @Test
    void updateTvShow_ShouldUpdateExistingTvShow() {
        when(tvShowRepository.findById(1L)).thenReturn(Optional.of(tvShow));
        when(tvShowRepository.save(tvShow)).thenReturn(tvShow);
        when(catalogMapper.tvShowToTvShowDTO(tvShow)).thenReturn(tvShowDTO);

        TvShowDTO result = tvShowService.updateTvShow(1L, tvShowDTO);

        assertNotNull(result);
        assertEquals(tvShowDTO.getTitle(), result.getTitle());

        verify(tvShowRepository).findById(1L);
        verify(catalogMapper).updateTvShowFromDTO(tvShowDTO, tvShow);
        verify(tvShowRepository).save(tvShow);
        verify(catalogMapper).tvShowToTvShowDTO(tvShow);
    }

    @Test
    void updateTvShow_ShouldThrowException_WhenTvShowDoesNotExist() {
        when(tvShowRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tvShowService.updateTvShow(999L, tvShowDTO));

        verify(tvShowRepository).findById(999L);
        verify(catalogMapper, never()).updateTvShowFromDTO(any(), any());
    }

    @Test
    void deleteTvShow_ShouldDeleteTvShow_WhenTvShowExists() {
        when(tvShowRepository.existsById(1L)).thenReturn(true);
        doNothing().when(tvShowRepository).deleteById(1L);

        assertDoesNotThrow(() -> tvShowService.deleteTvShow(1L));

        verify(tvShowRepository).existsById(1L);
        verify(tvShowRepository).deleteById(1L);
    }

    @Test
    void deleteTvShow_ShouldThrowException_WhenTvShowDoesNotExist() {
        when(tvShowRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> tvShowService.deleteTvShow(999L));

        verify(tvShowRepository).existsById(999L);
        verify(tvShowRepository, never()).deleteById(any());
    }
}