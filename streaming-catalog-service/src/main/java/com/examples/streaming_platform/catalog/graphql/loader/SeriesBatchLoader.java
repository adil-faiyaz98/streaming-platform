package com.examples.streaming_platform.catalog.graphql.loader;

import com.examples.streaming_platform.catalog.model.Series;
import com.examples.streaming_platform.catalog.repository.SeriesRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

/**
 * A DataLoader batch loader for Series, allowing batch fetching by a list of series IDs.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SeriesBatchLoader implements BatchLoader<Long, Series> {

    private final SeriesRepository seriesRepository;

    /**
     * Loads multiple Series entities by their IDs in a single query.
     * @param ids a list of series IDs to fetch
     * @return a CompletionStage with the corresponding Series objects
     */
    @Override
    public CompletionStage<List<Series>> load(List<Long> ids) {
        log.debug("Batch-loading Series with IDs: {}", ids);
        return CompletableFuture.supplyAsync(() -> seriesRepository.findAllById(ids));
    }
}
