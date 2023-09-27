package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem.reader.dto.Ecosystem;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem.reader.dto.EcosystemsResponse;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem.reader.mapper.EcosystemEntryMapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

/**
 * The type Ecosystem loader service.
 */
@Log4j2
@Service
public class EcosystemLoaderService {

    private final WebClient webClient;
    private final EcosystemQueryFactory queryFactory;
    private final EcosystemEntryMapper entryMapper;

    /**
     * Instantiates a new Ecosystem loader service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     * @param entryMapper the entry mapper
     */
    @Autowired
    public EcosystemLoaderService(WebClient webClient,
            EcosystemQueryFactory queryFactory,
            EcosystemEntryMapper entryMapper) {
        this.webClient = webClient;
        this.queryFactory = queryFactory;
        this.entryMapper = entryMapper;
    }

    /**
     * Fetch entries list.
     *
     * @return the list
     */
    public List<Entry> fetchEntries() {
        List<Entry> ecosystemEntries = fetchEcosystemEntries()
                .map(entryMapper::createEcosystemEntry)
                .collectList()
                .block(Duration.ofSeconds(30));
        log.debug("Fetched {} ecosystems entries", ecosystemEntries.size());
        return ecosystemEntries;
    }

    private Flux<Ecosystem> fetchEcosystemEntries() {
        return webClient
                .get()
                .uri(queryFactory.createEcosystemsUriQuery())
                .retrieve()
                .bodyToMono(EcosystemsResponse.class)
                .map(EcosystemsResponse::getEcosystems)
                .flatMapMany(Flux::fromIterable);
    }
}
