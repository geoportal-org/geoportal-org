package com.eversis.esa.geoss.curated.resources.model;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

/**
 * The type Source model.
 */
@Data
public class SourceModel {

    @NotNull
    private String term;

    @NotNull
    private String code;

}
