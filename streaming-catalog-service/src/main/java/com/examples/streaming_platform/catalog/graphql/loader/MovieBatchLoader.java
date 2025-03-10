package com.examples.streaming_platform.catalog.graphql.loader;

import com.examples.streaming_platform.catalog.model.Movie;
import com.examples.streaming_platform.catalog.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class MovieBatchLoader implements BatchLoader<Long, Movie> {

    private final MovieRepository movieRepository;

    @Override
    public CompletableFuture<List<Movie>> load(List<Long> ids) {
        log.debug("Loading movies with IDs: {}", ids);
        return CompletableFuture.supplyAsync(() -> movieRepository.findAllById(ids));
    }
}
