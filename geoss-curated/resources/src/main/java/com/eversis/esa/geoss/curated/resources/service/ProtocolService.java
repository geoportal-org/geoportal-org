package com.eversis.esa.geoss.curated.resources.service;

import com.eversis.esa.geoss.curated.resources.domain.Protocol;
import com.eversis.esa.geoss.curated.resources.model.ProtocolModel;

/**
 * The interface Protocol service.
 */
public interface ProtocolService {

    /**
     * Gets or create protocol.
     *
     * @param protocol the protocol
     * @return the or create protocol
     */
    Protocol getOrCreateProtocol(ProtocolModel protocol);

}
