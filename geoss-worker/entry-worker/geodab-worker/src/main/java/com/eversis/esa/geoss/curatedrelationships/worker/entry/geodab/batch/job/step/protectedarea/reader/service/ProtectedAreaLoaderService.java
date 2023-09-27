package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.reader.dto.ProtectedArea;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.reader.dto.ProtectedAreaResponse;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.reader.mapper.ProtectedAreaEntryMapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

/**
 * The type Protected area loader service.
 */
@Log4j2
@Service
public class ProtectedAreaLoaderService {

    private final WebClient webClient;
    private final ProtectedAreaQueryFactory queryFactory;
    private final ProtectedAreaEntryMapper entryMapper;

    /**
     * Instantiates a new Protected area loader service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     * @param entryMapper the entry mapper
     */
    @Autowired
    public ProtectedAreaLoaderService(WebClient webClient,
            ProtectedAreaQueryFactory queryFactory,
            ProtectedAreaEntryMapper entryMapper) {
        this.webClient = webClient;
        this.queryFactory = queryFactory;
        this.entryMapper = entryMapper;
    }

    /**
     * Fetch entries list.
     *
     * @param ecosystemId the ecosystem id
     * @return the list
     */
    public List<Entry> fetchEntries(String ecosystemId) {
        List<Entry> ecosystemEntries = fetchEcosystemEntries(ecosystemId)
                .map(protectedArea -> entryMapper.createEcosystemEntry(protectedArea, ecosystemId))
                .collectList()
                .block(Duration.ofSeconds(30));
        log.debug("Fetched {} protected area entries related to ecosystem: {}", ecosystemEntries.size(), ecosystemId);
        return ecosystemEntries;
    }

    private Flux<ProtectedArea> fetchEcosystemEntries(String ecosystemId) {
        return webClient
                .get()
                .uri(queryFactory.createEcosystemsUriQuery(ecosystemId))
                .retrieve()
                .bodyToMono(ProtectedAreaResponse.class)
                .map(ProtectedAreaResponse::getProtectedAreas)
                .flatMapMany(Flux::fromIterable);
    }
}
