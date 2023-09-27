package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.VlabDabProperties;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto.Run;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto.RunResponse;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.dto.RunStatus;

import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import jakarta.validation.constraints.Min;
import java.time.Duration;
import java.util.List;

/**
 * The type Run loader service.
 */
@Log4j2
@Service
@Validated
public class RunLoaderService {

    private static final String X_VLAB_API_KEY = "x-vlab-apikey";

    private final WebClient webClient;
    private final WorkflowQueryFactory queryFactory;
    private final VlabDabProperties vlabDabProperties;

    /**
     * Instantiates a new Run loader service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     * @param vlabDabProperties the vlab dab configuration
     */
    @Autowired
    public RunLoaderService(WebClient webClient,
            WorkflowQueryFactory queryFactory,
            VlabDabProperties vlabDabProperties) {
        this.webClient = webClient;
        this.queryFactory = queryFactory;
        this.vlabDabProperties = vlabDabProperties;
    }

    /**
     * Returns list of successfully completed runs.
     *
     * @param start the index of the first search result desired by the client
     * @param count the number of search results per page desired by the client
     * @return the list
     */
    public List<Run> fetchSuccessfullyCompletedRunEntries(@Min(0) int start, @Min(1) int count) {
        List<Run> runs = fetchRunEntries(start, count)
                .filterWhen(run -> hasRunCompletedSuccessfully(run.getRunId()))
                .collectList()
                .block(Duration.ofMinutes(2));
        log.info("Fetched {} completed runs", runs.size());
        return runs;
    }

    private Flux<Run> fetchRunEntries(@Min(0) int start, @Min(1) int count) {
        return webClient
                .get()
                .uri(queryFactory.createRunsUriQuery(start, count))
                .accept(MediaType.APPLICATION_JSON)
                .header(X_VLAB_API_KEY, vlabDabProperties.getApiToken())
                .retrieve()
                .bodyToMono(RunResponse.class)
                .flatMapIterable(RunResponse::getRuns)
                .filter(run -> StringUtils.hasLength(run.getRunId()))
                .timeout(Duration.ofSeconds(30));
    }

    private Mono<Boolean> hasRunCompletedSuccessfully(@NonNull String runId) {
        return webClient
                .get()
                .uri(queryFactory.createRunStatusUriQuery(runId))
                .accept(MediaType.APPLICATION_JSON)
                .header(X_VLAB_API_KEY, vlabDabProperties.getApiToken())
                .retrieve()
                .bodyToMono(RunStatus.class)
                .map(runStatus -> RunStatus.SUCCESSFULLY_COMPLETED.getStatus().equals(runStatus.getStatus())
                        && RunStatus.SUCCESSFULLY_COMPLETED.getResult().equals(runStatus.getResult()))
                .timeout(Duration.ofSeconds(30));
    }

}
