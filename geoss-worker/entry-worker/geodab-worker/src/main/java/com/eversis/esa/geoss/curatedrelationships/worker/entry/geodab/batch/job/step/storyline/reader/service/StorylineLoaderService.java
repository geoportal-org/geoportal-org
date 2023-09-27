package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.reader.dto.Storyline;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.reader.dto.StorylineResponse;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.reader.mapper.StorylineEntryMapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

/**
 * The type Storyline loader service.
 */
@Log4j2
@Service
public class StorylineLoaderService {

    private final WebClient webClient;
    private final StorylineQueryFactory queryFactory;
    private final StorylineEntryMapper entryMapper;

    /**
     * Instantiates a new Storyline loader service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     * @param entryMapper the entry mapper
     */
    @Autowired
    public StorylineLoaderService(WebClient webClient,
            StorylineQueryFactory queryFactory,
            StorylineEntryMapper entryMapper) {
        this.webClient = webClient;
        this.queryFactory = queryFactory;
        this.entryMapper = entryMapper;
    }

    /**
     * Fetch entries list.
     *
     * @param protectedAreaId the protected area id
     * @return the list
     */
    public List<Entry> fetchEntries(String protectedAreaId) {
        List<Entry> ecosystemEntries = fetchStorylineEntries(protectedAreaId)
                .map(protectedArea -> entryMapper.createStorylineEntry(protectedArea, protectedAreaId))
                .collectList()
                .block(Duration.ofSeconds(30));
        log.debug("Fetched {} storyline entries related to protectedArea: {}", ecosystemEntries.size(),
                protectedAreaId);
        return ecosystemEntries;
    }

    private Flux<Storyline> fetchStorylineEntries(String ecosystemId) {
        return webClient
                .get()
                .uri(queryFactory.createStorylinesUriQuery(ecosystemId))
                .retrieve()
                .bodyToMono(StorylineResponse.class)
                .map(StorylineResponse::getStorylines)
                .flatMapMany(Flux::fromIterable);
    }
}
