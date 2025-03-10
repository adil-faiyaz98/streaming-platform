package com.examples.streaming_platform.catalog.graphql.loader;

import com.examples.streaming_platform.catalog.model.Season;
import com.examples.streaming_platform.catalog.repository.SeasonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoader;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class SeasonBatchLoader implements BatchLoader<Long, Season> {

    private final SeasonRepository seasonRepository;

    @Override
    public CompletableFuture<List<Season>> load(List<Long> ids) {
        log.debug("Batch-loading Seasons with IDs: {}", ids);
        return CompletableFuture.supplyAsync(() -> seasonRepository.findAllById(ids));
    }
}
