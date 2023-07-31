package com.eversis.esa.geoss.curatedrelationships.search.repository;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Concept;

import javax.validation.constraints.NotNull;

public interface ThesaurusRepository {

    /**
     * Find curated relationships resources using search parameters.
     *
     * @param searchPhrase search phrase
     * @param pageable pagination information
     */
    Page<Concept> findConcepts(@NotNull String searchPhrase, @NotNull Pageable pageable);

}
