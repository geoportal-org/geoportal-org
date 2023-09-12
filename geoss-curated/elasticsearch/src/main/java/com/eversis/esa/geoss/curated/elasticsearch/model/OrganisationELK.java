package com.eversis.esa.geoss.curated.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

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
