package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.GeoDabProperties;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The type Geo dab workflow query factory.
 */
@Component
class GeoDabWorkflowQueryFactory {

    private final GeoDabProperties configuration;

    /**
     * Instantiates a new Geo dab workflow query factory.
     *
     * @param configuration the configuration
     */
    public GeoDabWorkflowQueryFactory(GeoDabProperties configuration) {
        this.configuration = configuration;
    }

    /**
     * Creates full uri for to endpoint, which provides all workflow entries.
     *
     * @return the string
     */
    public String createWorkflowsUriQuery() {
        return UriComponentsBuilder.fromHttpUrl(configuration.getBaseUrl() + configuration.getWorkflowsEndpoint())
                .queryParam("includeUnderTest", "true")
                .build(false).toUriString();
    }

    /**
     * Creates full uri for to endpoint, which provides workflow entries related to provided storyline.
     *
     * @param storylineId the storyline id
     * @return the string
     */
    public String createWorkflowsUriQuery(String storylineId) {
        return UriComponentsBuilder.fromHttpUrl(configuration.getBaseUrl() + configuration.getWorkflowsEndpoint())
                .queryParam("includeUnderTest", "true")
                .queryParam("storyline", storylineId)
                .build(false).toUriString();
    }
}
