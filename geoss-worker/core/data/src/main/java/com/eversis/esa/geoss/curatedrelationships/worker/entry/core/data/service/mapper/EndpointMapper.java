package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Endpoint;

/**
 * The type Endpoint mapper.
 */
public class EndpointMapper {

    private EndpointMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map endpoint endpoint.
     *
     * @param domainEndpoint the domain endpoint
     * @return the endpoint
     */
    public static Endpoint mapEndpoint(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Endpoint domainEndpoint) {
        Endpoint dbEndpoint = new Endpoint();
        dbEndpoint.setUrl(domainEndpoint.getUrl());
        dbEndpoint.setUrlType(domainEndpoint.getUrlType());
        return dbEndpoint;
    }

}
