package com.eversis.esa.geoss.curated.resources.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * The type Dashboard contents elk.
 */
@Data
@AllArgsConstructor
public class DashboardContentsELK implements Serializable {

    private String id;

    private String content;

}
