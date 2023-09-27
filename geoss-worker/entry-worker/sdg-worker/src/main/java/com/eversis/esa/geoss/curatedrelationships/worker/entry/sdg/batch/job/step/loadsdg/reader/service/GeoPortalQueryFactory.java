package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config.sdg.GeoPortalProperties;

import org.springframework.stereotype.Component;

/**
 * The type Geo portal query factory.
 */
@Component
class GeoPortalQueryFactory {

    private final GeoPortalProperties configuration;

    /**
     * Instantiates a new Geo portal query factory.
     *
     * @param configuration the configuration
     */
    public GeoPortalQueryFactory(GeoPortalProperties configuration) {
        this.configuration = configuration;
    }

    /**
     * Creates full uri for to endpoint, which provides all geo-portal sdg entries.
     *
     * @return the string
     */
    public String createGeoPortalSdgEntriesUriQuery() {
        return configuration.getBaseUrl() + configuration.getBaseJsonwsEndpoint()
               + "/geoss-service-portlet.filternode/get-all-nodes";
    }

}
