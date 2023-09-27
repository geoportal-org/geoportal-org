package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.VlabDabProperties;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * The type Workflow query factory.
 */
@Component
class WorkflowQueryFactory {

    private final VlabDabProperties configuration;

    /**
     * Instantiates a new Workflow query factory.
     *
     * @param configuration the configuration
     */
    public WorkflowQueryFactory(VlabDabProperties configuration) {
        this.configuration = configuration;
    }

    /**
     * Creates full uri for endpoint, which provides all runs.
     *
     * @param start the start
     * @param count the count
     * @return the uri
     */
    public URI createRunsUriQuery(int start, int count) {
        return UriComponentsBuilder.fromUriString(configuration.getBaseUrl() + configuration.getRunsEndpoint())
                .queryParam("start", start)
                .queryParam("count", count)
                .build(false).toUri();
    }

    /**
     * Creates full uri for endpoint, which returns status of the provided run.
     *
     * @param runId the run id
     * @return the uri
     */
    public URI createRunStatusUriQuery(String runId) {
        return UriComponentsBuilder.fromUriString(configuration.getBaseUrl() + configuration.getRunsEndpoint())
                .pathSegment("{runId}")
                .pathSegment("status")
                .build(runId);
    }

    /**
     * Creates full uri for endpoint, which returns outputs related to the provided run.
     *
     * @param runId the run id
     * @return the uri
     */
    public URI createRunOutputsUriQuery(String runId) {
        return UriComponentsBuilder.fromUriString(configuration.getBaseUrl() + configuration.getRunsEndpoint())
                .pathSegment("{runId}")
                .pathSegment("outputs")
                .build(runId);
    }

}
