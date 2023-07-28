package com.eversis.esa.geoss.curated.recommendations.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Recommendation model.
 */
@Data
public class RecommendationModel {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotEmpty
    @Valid
    private Set<RecommendedEntityModel> entities = new HashSet<>();

    @NotEmpty
    @Valid
    private Set<String> keywords = new HashSet<>();
}
