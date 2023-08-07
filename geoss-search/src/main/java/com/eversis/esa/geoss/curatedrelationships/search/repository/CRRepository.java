package com.eversis.esa.geoss.curatedrelationships.search.repository;

import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * The interface Cr repository.
 *
 * @param <T> the type parameter
 */
public interface CRRepository<T> {

    /**
     * Find curated relationships resources using search parameters.
     *
     * @param query optional filter parameters
     * @param pageable pagination information
     * @return the faceted page
     */
    FacetedPage<T> findResources(@NotNull SearchQuery query, @NotNull Pageable pageable);

    /**
     * Find curated relationships resources by their id.
     *
     * @param ids required parameter
     * @param pageable pagination information
     * @return the page
     */
    Page<T> findResourcesById(@NotNull Set<String> ids, @NotNull Pageable pageable);

}
