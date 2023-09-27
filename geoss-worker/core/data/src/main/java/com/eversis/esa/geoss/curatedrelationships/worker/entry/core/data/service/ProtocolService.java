package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Protocol;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository.ProtocolRepository;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper.ProtocolMapper;

import org.springframework.stereotype.Service;

/**
 * The type Protocol service.
 */
@Service
public class ProtocolService {

    private final ProtocolRepository protocolRepository;

    /**
     * Instantiates a new Protocol service.
     *
     * @param protocolRepository the protocol repository
     */
    public ProtocolService(ProtocolRepository protocolRepository) {
        this.protocolRepository = protocolRepository;
    }

    /**
     * Gets or create protocol.
     *
     * @param protocol the protocol
     * @return the or create protocol
     */
    public Protocol getOrCreateProtocol(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Protocol protocol) {
        return protocolRepository.findByValue(protocol.getValue())
                .orElseGet(() -> protocolRepository.save(ProtocolMapper.mapProtocol(protocol)));
    }

}
