package com.eversis.esa.geoss.curated.common.service;

import com.eversis.esa.geoss.curated.common.domain.Protocol;
import com.eversis.esa.geoss.curated.common.model.ProtocolModel;

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
