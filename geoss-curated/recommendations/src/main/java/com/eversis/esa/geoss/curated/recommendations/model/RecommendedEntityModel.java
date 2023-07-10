package com.eversis.esa.geoss.curated.recommendations.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

/**
 * The type Recommended entity model.
 */
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class RecommendedEntityModel {

    private Long id;

    @EqualsAndHashCode.Include
    @NotBlank
    private String dataSourceCode;

    @EqualsAndHashCode.Include
    @NotBlank
    private String entityCode;

    @NotBlank
    private String name;

    @Min(1)
    private double orderWeight = 1.0;
}
