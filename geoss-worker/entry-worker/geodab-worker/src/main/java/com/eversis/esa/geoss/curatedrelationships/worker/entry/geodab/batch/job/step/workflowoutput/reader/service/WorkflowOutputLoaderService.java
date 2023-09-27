package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.VlabDabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto.Run;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto.WorkflowOutputResponse;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.mapper.WorkflowOutputEntryMapper;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;

/**
 * The type Workflow output loader service.
 */
@Log4j2
@Service
@Validated
public class WorkflowOutputLoaderService {

    private static final String X_VLAB_API_KEY = "x-vlab-apikey";

    private final WebClient webClient;
    private final WorkflowQueryFactory queryFactory;
    private final WorkflowOutputEntryMapper entryMapper;
    private final VlabDabProperties vlabDabProperties;

    /**
     * Instantiates a new Workflow output loader service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     * @param entryMapper the entry mapper
     * @param vlabDabProperties the vlab dab configuration
     */
    @Autowired
    public WorkflowOutputLoaderService(WebClient webClient,
            WorkflowQueryFactory queryFactory,
            WorkflowOutputEntryMapper entryMapper,
            VlabDabProperties vlabDabProperties) {
        this.webClient = webClient;
        this.queryFactory = queryFactory;
        this.entryMapper = entryMapper;
        this.vlabDabProperties = vlabDabProperties;
    }

    /**
     * Fetch entries list.
     *
     * @param run the run
     * @return the list
     */
    public List<Entry> fetchEntries(@NonNull Run run) {
        List<Entry> workflowOutputs = fetchRunOutputs(run)
                .doOnError(WebClientResponseException.class, e -> {
                    log.error(e.getMessage() + " " + e.getResponseBodyAsString());
                })
                .collectList()
                .block(Duration.ofMinutes(1));
        log.info("Fetched {} workflow output entries related to "
                + "runId: {} "
                + "and workflowId: {}", workflowOutputs.size(), run.getRunId(), run.getWorkflowId());
        return workflowOutputs;
    }

    private Flux<Entry> fetchRunOutputs(@NonNull Run run) {
        log.debug("Fetching output of related to runId: {} and workflowId: {}", run.getRunId(), run.getWorkflowId());
        return webClient
                .get()
                .uri(queryFactory.createRunOutputsUriQuery(run.getRunId()))
                .accept(MediaType.APPLICATION_JSON)
                .header(X_VLAB_API_KEY, vlabDabProperties.getApiToken())
                .retrieve()
                .bodyToMono(WorkflowOutputResponse.class)
                .flatMapIterable(WorkflowOutputResponse::getOutputs)
                .map(workflowOutput -> entryMapper.createWorkflowOutputEntry(workflowOutput, run.getWorkflowId()))
                .timeout(Duration.ofSeconds(30));
    }

}
