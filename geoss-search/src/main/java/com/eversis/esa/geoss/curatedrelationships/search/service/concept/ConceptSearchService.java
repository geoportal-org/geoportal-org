package com.eversis.esa.geoss.curatedrelationships.search.service.concept;

import java.util.List;
import javax.validation.constraints.NotNull;

public interface ConceptSearchService {

    /**
     * Search for concepts related to search phrase. Business rules may be applied to the search options.
     *
     * @param phrase list of entry's ids filter parameters
     * @param count max number of concepts - value has to be greater than 0
     */
    List<String> findConceptNames(@NotNull String phrase, int count);
}
