package com.eversis.esa.geoss.curated.extensions.model;

import com.eversis.esa.geoss.curated.common.model.EndpointModel;
import com.eversis.esa.geoss.curated.common.model.ProtocolModel;

import lombok.Data;

/**
 * The type Transfer option extension model.
 */
@Data
public class TransferOptionExtensionModel {

    private Long id;

    private String name;

    private String description;

    private String displayTitle;

    private ProtocolModel protocol;

    private EndpointModel endpoint;

}
