package com.eversis.esa.geoss.curatedrelationships.search.model.common;

import java.util.List;
import java.util.Map;

/**
 * The interface Faceted page.
 *
 * @param <T> the type parameter
 */
public interface FacetedPage<T> extends Page<T> {

    /**
     * Gets facets.
     *
     * @return the facets
     */
    Map<String, List<Facet>> getFacets();

}
