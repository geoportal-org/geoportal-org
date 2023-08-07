package com.eversis.esa.geoss.curatedrelationships.search.service.concept;

import jakarta.validation.constraints.NotNull;
import java.util.List;

/**
 * The interface Concept search service.
 */
public interface ConceptSearchService {

    /**
     * Search for concepts related to search phrase. Business rules may be applied to the search options.
     *
     * @param phrase list of entry's ids filter parameters
     * @param count max number of concepts - value has to be greater than 0
     * @return the list
     */
    List<String> findConceptNames(@NotNull String phrase, int count);
}
