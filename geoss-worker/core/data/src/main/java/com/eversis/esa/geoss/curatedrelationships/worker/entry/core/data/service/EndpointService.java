package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Endpoint;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository.EndpointRepository;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper.EndpointMapper;

import org.springframework.stereotype.Service;

/**
 * The type Endpoint service.
 */
@Service
public class EndpointService {

    private final EndpointRepository endpointRepository;

    /**
     * Instantiates a new Endpoint service.
     *
     * @param endpointRepository the endpoint repository
     */
    public EndpointService(EndpointRepository endpointRepository) {
        this.endpointRepository = endpointRepository;
    }

    /**
     * Gets or create endpoint.
     *
     * @param endpoint the endpoint
     * @return the or create endpoint
     */
    public Endpoint getOrCreateEndpoint(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Endpoint endpoint) {
        return endpointRepository.findByUrlAndUrlType(endpoint.getUrl(), endpoint.getUrlType())
                .orElseGet(() -> endpointRepository.save(EndpointMapper.mapEndpoint(endpoint)));
    }

}
