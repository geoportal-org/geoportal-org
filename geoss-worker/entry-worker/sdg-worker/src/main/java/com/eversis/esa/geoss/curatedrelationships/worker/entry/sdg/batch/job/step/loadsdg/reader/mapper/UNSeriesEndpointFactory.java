package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.reader.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Endpoint;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.config.sdg.UNSdgProperties;

import org.springframework.stereotype.Component;

/**
 * The type Un series endpoint factory.
 */
@Component
class UNSeriesEndpointFactory {

    private final UNSdgProperties unSdgProperties;

    /**
     * Instantiates a new Un series endpoint factory.
     *
     * @param unSdgProperties the un sdg configuration
     */
    public UNSeriesEndpointFactory(UNSdgProperties unSdgProperties) {
        this.unSdgProperties = unSdgProperties;
    }

    /**
     * Create un series endpoint endpoint.
     *
     * @param seriesCode the series code
     * @return the endpoint
     */
    public Endpoint createUNSeriesEndpoint(String seriesCode) {
        return new Endpoint(getUNSeriesUrl(unSdgProperties.getBaseUrl(), seriesCode), "HTTP");
    }

    private String getUNSeriesUrl(String baseApiUrl, String seriesCode) {
        return baseApiUrl + "/Series/Data?seriesCode=" + seriesCode;
    }

}
