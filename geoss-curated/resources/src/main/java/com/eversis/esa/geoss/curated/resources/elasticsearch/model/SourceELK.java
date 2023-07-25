package com.eversis.esa.geoss.curated.resources.elasticsearch.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Source elk.
 */
@Data
@AllArgsConstructor
public class SourceELK implements Serializable {

    private String term;

    private String sourceId;

}
