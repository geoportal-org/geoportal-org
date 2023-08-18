package com.eversis.esa.geoss.curated.resources.model;

import lombok.Data;

/**
 * The type Transfer option model.
 */
@Data
public class TransferOptionModel {

    private String name;

    private String description;

    private String title;

    private ProtocolModel protocol;

    private EndpointModel endpoint;

}
