package com.eversis.esa.geoss.curatedrelationships.search.model.recommendation;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * The type Recommendation.
 */
@Data
public class Recommendation implements Serializable {

    private final List<RecommendedResource> entities;

    /**
     * Instantiates a new Recommendation.
     *
     * @param entities the entities
     */
    public Recommendation(List<RecommendedResource> entities) {
        this.entities = entities != null ? entities : Collections.emptyList();
    }

    /**
     * Is empty boolean.
     *
     * @return the boolean
     */
    public boolean isEmpty() {
        return entities.isEmpty();
    }
}
