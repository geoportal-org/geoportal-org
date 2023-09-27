package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.repository;

import com.eversis.esa.geoss.curatedrelationships.thesaurusworker.model.Concept;

import jakarta.validation.constraints.NotNull;

/**
 * The interface Concept repository.
 */
public interface ConceptRepository {

    /**
     * Saves concept in data store.
     *
     * @param concept concept to save
     */
    void saveConcept(@NotNull Concept concept);

}
