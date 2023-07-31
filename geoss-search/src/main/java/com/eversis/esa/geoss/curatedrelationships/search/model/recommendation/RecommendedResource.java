package com.eversis.esa.geoss.curatedrelationships.search.model.recommendation;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;

import lombok.Data;

import java.io.Serializable;

/**
 * The type Recommended resource.
 */
@Data
public class RecommendedResource implements Serializable {

    private final DataSource dataSource;
    private final String code;
    private final String name;
    private final String description;
    private final double orderWeight;

}
