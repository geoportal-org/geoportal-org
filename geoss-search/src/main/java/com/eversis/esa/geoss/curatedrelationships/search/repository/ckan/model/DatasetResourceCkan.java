package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model;

import lombok.Data;

/**
 * The type Dataset resource ckan.
 */
@Data
public class DatasetResourceCkan {

    private String id;
    private String name;
    private String description;
    private String url;
    private String format;

}
