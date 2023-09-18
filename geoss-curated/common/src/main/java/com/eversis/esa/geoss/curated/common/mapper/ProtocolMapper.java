package com.eversis.esa.geoss.curated.common.mapper;

import com.eversis.esa.geoss.curated.common.domain.Protocol;
import com.eversis.esa.geoss.curated.common.model.ProtocolModel;

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
     * @param protocol the protocol
     * @return the protocol
     */
    public static Protocol mapProtocol(ProtocolModel protocol) {
        Protocol dbProtocol = new Protocol(protocol.getValue());
        dbProtocol.setIsCustom(1);
        return dbProtocol;
    }

}
