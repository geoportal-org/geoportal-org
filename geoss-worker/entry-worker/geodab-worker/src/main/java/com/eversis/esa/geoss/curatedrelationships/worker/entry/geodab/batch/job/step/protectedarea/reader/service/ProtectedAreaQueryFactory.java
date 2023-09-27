package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.GeoDabProperties;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The type Protected area query factory.
 */
@Component
class ProtectedAreaQueryFactory {

    private final GeoDabProperties configuration;

    /**
     * Instantiates a new Protected area query factory.
     *
     * @param configuration the configuration
     */
    public ProtectedAreaQueryFactory(GeoDabProperties configuration) {
        this.configuration = configuration;
    }

    /**
     * Creates full uri for to endpoint, which provides protected areas entries related to provided ecosystem.
     *
     * @param ecosystemId the ecosystem id
     * @return the string
     */
    public String createEcosystemsUriQuery(String ecosystemId) {
        return UriComponentsBuilder.fromHttpUrl(configuration.getBaseUrl() + configuration.getProtectedAreasEndpoint())
                .queryParam("ecosystem", ecosystemId)
                .build(false).toUriString();
    }
}
