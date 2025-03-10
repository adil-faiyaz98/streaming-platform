package com.examples.streaming_platform.catalog.graphql.loader;

import com.examples.streaming_platform.catalog.controller.SeriesController;
import com.examples.streaming_platform.catalog.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeriesBatchLoader implements BatchLoader<Long, SeriesController> {

    private final SeriesRepository seriesRepository;

    @Override
    public CompletableFuture<List<SeriesController>> load(List<Long> ids) {
        log.debug("Batch-loading SeriesController with IDs: {}", ids);
        return CompletableFuture.supplyAsync(() -> seriesRepository.findAllById(ids));
    }
}
