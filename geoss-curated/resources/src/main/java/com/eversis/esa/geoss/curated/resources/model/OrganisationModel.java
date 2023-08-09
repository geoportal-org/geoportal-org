package com.eversis.esa.geoss.curated.resources.model;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 * The type Organisation model.
 */
@Data
public class OrganisationModel {

    @NotNull
    private String title;

    private String email;

    private String contact;

    private String contactEmail;

}
