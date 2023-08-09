package com.eversis.esa.geoss.curated.resources.model;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

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
