package com.eversis.esa.geoss.curated.common.service.impl;

import com.eversis.esa.geoss.curated.common.domain.Protocol;
import com.eversis.esa.geoss.curated.common.mapper.ProtocolMapper;
import com.eversis.esa.geoss.curated.common.model.ProtocolModel;
import com.eversis.esa.geoss.curated.common.repository.ProtocolRepository;
import com.eversis.esa.geoss.curated.common.service.ProtocolService;

import org.springframework.stereotype.Service;

/**
 * The type Protocol service.
 */
@Service
public class ProtocolServiceImpl implements ProtocolService {

    private final ProtocolRepository protocolRepository;

    /**
     * Instantiates a new Protocol service.
     *
     * @param protocolRepository the protocol repository
     */
    public ProtocolServiceImpl(ProtocolRepository protocolRepository) {
        this.protocolRepository = protocolRepository;
    }

    @Override
    public Protocol getOrCreateProtocol(ProtocolModel protocol) {
        return protocolRepository.findByValue(protocol.getValue())
                .orElseGet(() -> protocolRepository.save(ProtocolMapper.mapProtocol(protocol)));
    }

}
