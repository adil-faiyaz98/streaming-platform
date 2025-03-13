package com.examples.streaming_platform.catalog.graphql.loader;

import com.examples.streaming_platform.catalog.model.TvShow;
import com.examples.streaming_platform.catalog.repository.TvShowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
@RequiredArgsConstructor
public class TvShowBatchLoader implements BatchLoader<Long, TvShow> {

    private final TvShowRepository tvShowRepository;

    @Override
    public CompletableFuture<List<TvShow>> load(List<Long> ids) {
        log.debug("Loading TV Shows with IDs: {}", ids);
        return CompletableFuture.supplyAsync(() -> tvShowRepository.findAllById(ids));
    }
}
