package com.eversis.esa.geoss.curated.resources.model;

import lombok.Data;

import jakarta.validation.constraints.NotNull;

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
