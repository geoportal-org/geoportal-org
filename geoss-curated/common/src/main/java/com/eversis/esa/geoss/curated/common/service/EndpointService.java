package com.eversis.esa.geoss.curated.common.service;

import com.eversis.esa.geoss.curated.common.domain.Endpoint;
import com.eversis.esa.geoss.curated.common.model.EndpointModel;

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
