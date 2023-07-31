package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.Facets;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.model.base.CkanResponse;

import java.util.Map;

public interface CkanResponseMapper<T, S> {

    /**
     * Wraps CKAN response to custom page object including facets.
     *
     * @param searchResponse CKAN response
     * @param pageable pagination information
     */
    Page<T> mapPackageSearchResponse(CkanResponse<S> searchResponse, Pageable pageable);

    /**
     * Wraps CKAN response to custom page object including facets.
     *
     * @param searchResponse CKAN response
     * @param pageable pagination information
     * @param facetFields facet fields names and display names
     */
    FacetedPage<T> mapFacetedPackageSearchResponse(CkanResponse<S> searchResponse, Pageable pageable, Map<String, Facets> facetFields);

}
