package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.service.mapper;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model.Protocol;

/**
 * The type Protocol mapper.
 */
public class ProtocolMapper {

    private ProtocolMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map protocol protocol.
     *
     * @param domainProtocol the domain protocol
     * @return the protocol
     */
    public static Protocol mapProtocol(
            com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Protocol domainProtocol) {
        return new Protocol(domainProtocol.getValue());
    }

}
