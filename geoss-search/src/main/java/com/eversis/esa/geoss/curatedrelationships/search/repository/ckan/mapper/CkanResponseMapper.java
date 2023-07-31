package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.Facets;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.base.CkanResponse;

import java.util.Map;

/**
 * The interface Ckan response mapper.
 *
 * @param <T> the type parameter
 * @param <S> the type parameter
 */
public interface CkanResponseMapper<T, S> {

    /**
     * Wraps CKAN response to custom page object including facets.
     *
     * @param searchResponse CKAN response
     * @param pageable pagination information
     * @return the page
     */
    Page<T> mapPackageSearchResponse(CkanResponse<S> searchResponse, Pageable pageable);

    /**
     * Wraps CKAN response to custom page object including facets.
     *
     * @param searchResponse CKAN response
     * @param pageable pagination information
     * @param facetFields facet fields names and display names
     * @return the faceted page
     */
    FacetedPage<T> mapFacetedPackageSearchResponse(CkanResponse<S> searchResponse, Pageable pageable,
            Map<String, Facets> facetFields);

}
