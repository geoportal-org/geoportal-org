package com.eversis.esa.geoss.curated.recommendations.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Schema(example = "geoss_cr")
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
