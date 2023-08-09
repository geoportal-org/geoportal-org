package com.eversis.esa.geoss.curated.resources.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * The type Source elk.
 */
@Data
@AllArgsConstructor
public class SourceELK implements Serializable {

    private String term;

    private String sourceId;

}
