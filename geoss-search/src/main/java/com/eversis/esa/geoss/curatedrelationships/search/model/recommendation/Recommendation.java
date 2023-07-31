package com.eversis.esa.geoss.curatedrelationships.search.model.recommendation;

import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
public class Recommendation implements Serializable {

    private final List<RecommendedResource> entities;

    public Recommendation(List<RecommendedResource> entities) {
        this.entities = entities != null ? entities : Collections.emptyList();
    }

    public boolean isEmpty() {
        return entities.isEmpty();
    }
}
