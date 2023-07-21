package com.eversis.esa.geoss.curated.resources.model;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

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
