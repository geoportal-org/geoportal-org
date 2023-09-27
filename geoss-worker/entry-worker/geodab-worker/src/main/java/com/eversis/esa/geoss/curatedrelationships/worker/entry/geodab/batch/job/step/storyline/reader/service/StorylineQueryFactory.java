package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.storyline.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.config.geodab.GeoDabProperties;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The type Storyline query factory.
 */
@Component
class StorylineQueryFactory {

    private final GeoDabProperties configuration;

    /**
     * Instantiates a new Storyline query factory.
     *
     * @param configuration the configuration
     */
    public StorylineQueryFactory(GeoDabProperties configuration) {
        this.configuration = configuration;
    }

    /**
     * Creates full uri for to endpoint, which provides storyline entries related to provided protected area.
     *
     * @param protectedAreaId the protected area id
     * @return the string
     */
    public String createStorylinesUriQuery(String protectedAreaId) {
        return UriComponentsBuilder.fromHttpUrl(configuration.getBaseUrl() + configuration.getStorylinesEndpoint())
                .queryParam("protectedArea", protectedAreaId)
                .build(false).toUriString();
    }
}
