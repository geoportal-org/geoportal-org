package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.Endpoint;
import com.eversis.esa.geoss.curated.resources.model.EndpointModel;

/**
 * The interface Endpoint service.
 */
public interface EndpointService {

    /**
     * Gets or create endpoint.
     *
     * @param endpoint the endpoint
     * @return the or create endpoint
     */
    Endpoint getOrCreateEndpoint(EndpointModel endpoint);

}
