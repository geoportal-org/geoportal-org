package com.eversis.esa.geoss.curated.resources.elasticsearch.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Organisation elk.
 */
@Data
@AllArgsConstructor
public class OrganisationELK implements Serializable {

    private String id;

    private String title;

    private String contact;

    private String email;

    private String contactEmail;

}
