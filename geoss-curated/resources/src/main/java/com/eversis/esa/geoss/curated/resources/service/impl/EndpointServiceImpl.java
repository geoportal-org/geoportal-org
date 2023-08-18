package com.eversis.esa.geoss.curated.resources.service.impl;

import com.eversis.esa.geoss.curated.resources.domain.Endpoint;
import com.eversis.esa.geoss.curated.resources.mapper.EndpointMapper;
import com.eversis.esa.geoss.curated.resources.model.EndpointModel;
import com.eversis.esa.geoss.curated.resources.repository.EndpointRepository;
import com.eversis.esa.geoss.curated.resources.service.EndpointService;
import org.springframework.stereotype.Service;

/**
 * The type Endpoint service.
 */
@Service
public class EndpointServiceImpl implements EndpointService {

    private final EndpointRepository endpointRepository;

    /**
     * Instantiates a new Endpoint service.
     *
     * @param endpointRepository the endpoint repository
     */
    public EndpointServiceImpl(EndpointRepository endpointRepository) {
        this.endpointRepository = endpointRepository;
    }

    @Override
    public Endpoint getOrCreateEndpoint(EndpointModel endpoint) {
        return endpointRepository.findByUrlAndUrlType(endpoint.getUrl(), endpoint.getUrlType())
                .orElseGet(() -> endpointRepository.save(EndpointMapper.mapEndpoint(endpoint)));
    }
}
