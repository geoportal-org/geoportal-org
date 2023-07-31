package com.eversis.esa.geoss.curatedrelationships.search.model.entity;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class TransferOptionExtension implements Serializable {

    private final Integer entryExtensionId;
    private final String name;

    private String displayTitle;
    private String description;
    private String urlType;
    private String url;
    private String protocol;

}
