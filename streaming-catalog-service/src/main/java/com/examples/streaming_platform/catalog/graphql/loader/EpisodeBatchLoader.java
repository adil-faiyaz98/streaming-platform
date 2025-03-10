package com.examples.streaming_platform.catalog.graphql.loader;

import com.examples.streaming_platform.catalog.model.Episode;
import com.examples.streaming_platform.catalog.repository.EpisodeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class EpisodeBatchLoader implements BatchLoader<Long, Episode> {

    private final EpisodeRepository episodeRepository;

    @Override
    public CompletableFuture<List<Episode>> load(List<Long> ids) {
        log.debug("Batch-loading Episodes with IDs: {}", ids);
        return CompletableFuture.supplyAsync(() -> episodeRepository.findAllById(ids));
    }
}
