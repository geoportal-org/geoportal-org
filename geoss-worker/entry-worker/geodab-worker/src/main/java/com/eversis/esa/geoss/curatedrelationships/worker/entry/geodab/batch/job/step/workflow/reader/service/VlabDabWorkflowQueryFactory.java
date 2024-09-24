package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.VlabDabProperties;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The type Vlab dab workflow query factory.
 */
@Component
class VlabDabWorkflowQueryFactory {

    private final VlabDabProperties configuration;

    /**
     * Instantiates a new Vlab dab workflow query factory.
     *
     * @param configuration the configuration
     */
    public VlabDabWorkflowQueryFactory(VlabDabProperties configuration) {
        this.configuration = configuration;
    }

    /**
     * Creates full uri for to endpoint, which provides all workflow entries.
     *
     * @param start the start
     * @param count the count
     * @return the string
     */
    public String createWorkflowsUriQuery(int start, int count) {
        return UriComponentsBuilder.fromHttpUrl(configuration.getBaseUrl() + configuration.getWorkflowsEndpoint())
                .queryParam("includeUnderTest", configuration.getWorkflows().isIncludeUnderTest())
                .queryParam("start", start)
                .queryParam("count", count)
                .build(false).toUriString();
    }
}
