package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.GeoDabProperties;

import org.springframework.stereotype.Component;

/**
 * The type Ecosystem query factory.
 */
@Component
class EcosystemQueryFactory {

    private final GeoDabProperties configuration;

    /**
     * Instantiates a new Ecosystem query factory.
     *
     * @param configuration the configuration
     */
    public EcosystemQueryFactory(GeoDabProperties configuration) {
        this.configuration = configuration;
    }

    /**
     * Creates full uri for to endpoint, which provides all ecosystems entries.
     *
     * @return the string
     */
    public String createEcosystemsUriQuery() {
        return configuration.getBaseUrl() + configuration.getEcosystemsEndpoint();
    }
}
