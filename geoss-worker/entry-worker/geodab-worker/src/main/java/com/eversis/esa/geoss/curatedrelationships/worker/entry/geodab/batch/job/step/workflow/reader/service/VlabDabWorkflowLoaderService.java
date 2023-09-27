package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.VlabDabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.dto.Workflow;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.dto.WorkflowResponse;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.mapper.WorkflowEntryMapper;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import jakarta.validation.constraints.Min;
import java.time.Duration;
import java.util.List;

/**
 * The type Vlab dab workflow loader service.
 */
@Log4j2
@Service
@Validated
public class VlabDabWorkflowLoaderService {

    private static final String X_VLAB_API_KEY = "x-vlab-apikey";

    private final WebClient webClient;
    private final VlabDabWorkflowQueryFactory queryFactory;
    private final WorkflowEntryMapper entryMapper;
    private final VlabDabProperties vlabDabProperties;

    /**
     * Instantiates a new Vlab dab workflow loader service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     * @param entryMapper the entry mapper
     * @param vlabDabProperties the vlab dab configuration
     */
    @Autowired
    public VlabDabWorkflowLoaderService(WebClient webClient,
            VlabDabWorkflowQueryFactory queryFactory,
            WorkflowEntryMapper entryMapper,
            VlabDabProperties vlabDabProperties) {
        this.webClient = webClient;
        this.queryFactory = queryFactory;
        this.entryMapper = entryMapper;
        this.vlabDabProperties = vlabDabProperties;
    }

    /**
     * Fetch entries list.
     *
     * @param start the start
     * @param count the count
     * @return the list
     */
    public List<Entry> fetchEntries(@Min(0) int start, @Min(1) int count) {
        List<Entry> ecosystemEntries = fetchWorkflowEntries(start, count)
                .map(entryMapper::createWorkflowEntry)
                .collectList()
                .block(Duration.ofSeconds(360));
        log.debug("Fetched {} workflow entries", ecosystemEntries.size());
        return ecosystemEntries;
    }

    private Flux<Workflow> fetchWorkflowEntries(@Min(0) int start, @Min(1) int count) {
        return webClient
                .get()
                .uri(queryFactory.createWorkflowsUriQuery(start, count))
                .accept(MediaType.APPLICATION_JSON)
                .header(X_VLAB_API_KEY, vlabDabProperties.getApiToken())
                .retrieve()
                .bodyToMono(WorkflowResponse.class)
                .map(WorkflowResponse::getWorkflows)
                .flatMapMany(Flux::fromIterable)
                .timeout(Duration.ofSeconds(30));
    }

}
