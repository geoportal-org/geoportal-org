package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;

import org.elasticsearch.action.search.SearchResponse;

/**
 * The interface Faceted elasticsearch response mapper.
 *
 * @param <T> the type parameter
 */
public interface FacetedElasticsearchResponseMapper<T> extends ElasticsearchResponseMapper<T> {

    /**
     * Wrap elasticsearch response to custom page object including facets.
     *
     * @param searchResponse Elasticsearch response
     * @param pageable pagination information
     * @return the faceted page
     */
    FacetedPage<T> mapFacetedSearchResponse(SearchResponse searchResponse, Pageable pageable);

}
