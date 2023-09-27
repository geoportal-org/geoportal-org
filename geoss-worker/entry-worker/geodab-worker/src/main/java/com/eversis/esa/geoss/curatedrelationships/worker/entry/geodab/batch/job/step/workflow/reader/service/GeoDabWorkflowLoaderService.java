package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.dto.Workflow;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.dto.WorkflowResponse;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.mapper.WorkflowEntryMapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

/**
 * The type Geo dab workflow loader service.
 */
@Log4j2
@Service
public class GeoDabWorkflowLoaderService {

    private final WebClient webClient;
    private final GeoDabWorkflowQueryFactory queryFactory;
    private final WorkflowEntryMapper entryMapper;

    /**
     * Instantiates a new Geo dab workflow loader service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     * @param entryMapper the entry mapper
     */
    @Autowired
    public GeoDabWorkflowLoaderService(WebClient webClient,
            GeoDabWorkflowQueryFactory queryFactory,
            WorkflowEntryMapper entryMapper) {
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
        List<Entry> ecosystemEntries = fetchWorkflowEntries()
                .map(entryMapper::createWorkflowEntry)
                .collectList()
                .block(Duration.ofSeconds(30));
        log.debug("Fetched {} workflow entries", ecosystemEntries.size());
        return ecosystemEntries;
    }

    /**
     * Fetch entries list.
     *
     * @param storylineId the storyline id
     * @return the list
     */
    public List<Entry> fetchEntries(String storylineId) {
        List<Entry> ecosystemEntries = fetchWorkflowEntries(storylineId)
                .map(protectedArea -> entryMapper.createWorkflowEntry(protectedArea, storylineId))
                .collectList()
                .block(Duration.ofSeconds(30));
        log.debug("Fetched {} workflow entries related to storyline: {}", ecosystemEntries.size(), storylineId);
        return ecosystemEntries;
    }

    private Flux<Workflow> fetchWorkflowEntries() {
        return webClient
                .get()
                .uri(queryFactory.createWorkflowsUriQuery())
                .retrieve()
                .bodyToMono(WorkflowResponse.class)
                .map(WorkflowResponse::getWorkflows)
                .flatMapMany(Flux::fromIterable);
    }

    private Flux<Workflow> fetchWorkflowEntries(String storylineId) {
        return webClient
                .get()
                .uri(queryFactory.createWorkflowsUriQuery(storylineId))
                .retrieve()
                .bodyToMono(WorkflowResponse.class)
                .map(WorkflowResponse::getWorkflows)
                .flatMapMany(Flux::fromIterable);
    }
}
