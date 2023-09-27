package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config.sdg.UNSdgProperties;

import org.springframework.stereotype.Component;

/**
 * The type Un query factory.
 */
@Component
class UNQueryFactory {

    private final UNSdgProperties configuration;

    /**
     * Instantiates a new Un query factory.
     *
     * @param configuration the configuration
     */
    public UNQueryFactory(UNSdgProperties configuration) {
        this.configuration = configuration;
    }

    /**
     * Creates full uri for to endpoint, which provides all un-sdg entries.
     *
     * @return the string
     */
    public String createUNEntriesUriQuery() {
        return configuration.getBaseUrl() + configuration.getGoalsEndpoint();
    }
}
