package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Transfer option.
 */
@Getter
@Setter
public class TransferOption {

    private String name;
    private String description;
    private String title;
    private Protocol protocol;
    private Endpoint endpoint;
    private Entry entry;

}
