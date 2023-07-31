package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model;

import lombok.Data;

import java.io.Serializable;

/**
 * The type Recommended resource elk.
 */
@Data
public class RecommendedResourceElk implements Serializable {

    private String dataSource;
    private String code;
    private String name;
    private String description;
    private double orderWeight = 1.0;

}
