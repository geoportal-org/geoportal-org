package com.eversis.esa.geoss.curated.resources.model;

import com.eversis.esa.geoss.curated.common.model.EndpointModel;
import com.eversis.esa.geoss.curated.common.model.ProtocolModel;

import lombok.Data;

/**
 * The type Transfer option model.
 */
@Data
public class TransferOptionModel {

    private Long id;

    private String name;

    private String description;

    private String title;

    private ProtocolModel protocol;

    private EndpointModel endpoint;

}
