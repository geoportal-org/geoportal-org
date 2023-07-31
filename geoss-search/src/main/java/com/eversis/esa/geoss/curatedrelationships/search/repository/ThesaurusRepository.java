package com.eversis.esa.geoss.curatedrelationships.search.repository;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Concept;

import javax.validation.constraints.NotNull;

/**
 * The interface Thesaurus repository.
 */
public interface ThesaurusRepository {

    /**
     * Find curated relationships resources using search parameters.
     *
     * @param searchPhrase search phrase
     * @param pageable pagination information
     * @return the page
     */
    Page<Concept> findConcepts(@NotNull String searchPhrase, @NotNull Pageable pageable);

}
