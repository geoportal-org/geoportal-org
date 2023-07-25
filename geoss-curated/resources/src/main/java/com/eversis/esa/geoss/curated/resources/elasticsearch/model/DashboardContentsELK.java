package com.eversis.esa.geoss.curated.resources.elasticsearch.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Dashboard contents elk.
 */
@Data
@AllArgsConstructor
public class DashboardContentsELK implements Serializable {

    private String id;

    private String content;

}
