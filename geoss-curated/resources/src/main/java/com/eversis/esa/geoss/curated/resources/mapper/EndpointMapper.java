package com.eversis.esa.geoss.curated.resources.mapper;

import com.eversis.esa.geoss.curated.resources.domain.Endpoint;
import com.eversis.esa.geoss.curated.resources.model.EndpointModel;

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
    public static Endpoint mapEndpoint(EndpointModel domainEndpoint) {
        Endpoint dbEndpoint = new Endpoint(domainEndpoint.getUrl());
        dbEndpoint.setUrlType(domainEndpoint.getUrlType());
        dbEndpoint.setIsCustom(1);
        return dbEndpoint;
    }

}
