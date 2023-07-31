package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferOptionELK {

    private String id;
    private String name;
    private String title;
    private String description;
    private String urlType;
    private String url;
    private String protocol;

}
