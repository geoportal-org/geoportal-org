package com.eversis.esa.geoss.curated.recommendations.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * The type Recommendation model.
 */
@Data
public class RecommendationModel {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Valid
    private Set<RecommendedEntityModel> entities = new HashSet<>();

    @Valid
    private Set<String> keywords = new HashSet<>();

    /**
     * Validate.
     */
    public void validate() {
        if (entities.isEmpty()) {
            throw new ConstraintViolationException("Unable to create recommendation without linked entities",
                    Collections.emptySet());
        }

        if (keywords.isEmpty()) {
            throw new ConstraintViolationException("Unable to create recommendation without linked keywords",
                    Collections.emptySet());
        }
    }
}
