package com.examples.streaming_platform.catalog.dto;

import com.examples.streaming_platform.catalog.model.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.*;

/**
 * DTO representing a TV SeriesController entity.
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeriesDTO {

    private Long id;

    @NotBlank(message = "Title is required.")
    @Size(max = 255, message = "Title must be less than 255 characters.")
    private String title;

    private String description;
    private Integer startYear;
    private Integer endYear;

    private String maturityRating;
    private String imageUrl;

    @Builder.Default
    private Double averageRating = 0.0;

    @Builder.Default
    private Long viewCount = 0L;

    @Builder.Default
    private Boolean featured = false;

    @Builder.Default
    private Set<Genre> genres = new Set<Genre>() {
        @Override
        public int size() {
            return 0;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public boolean contains(Object o) {
            return false;
        }

        @Override
        public Iterator<Genre> iterator() {
            return null;
        }

        @Override
        public Object[] toArray() {
            return new Object[0];
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return null;
        }

        @Override
        public boolean add(Genre genre) {
            return false;
        }

        @Override
        public boolean remove(Object o) {
            return false;
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean addAll(Collection<? extends Genre> c) {
            return false;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return false;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return false;
        }

        @Override
        public void clear() {

        }
    };

    @Builder.Default
    private List<SeasonDTO> seasons = new ArrayList<>();

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
