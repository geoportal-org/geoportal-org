package com.eversis.esa.geoss.curated.resources.model;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

/**
 * The type Access policy model.
 */
@Data
public class AccessPolicyModel {

    @NotNull
    private String name;

    @NotNull
    private String code;

}
