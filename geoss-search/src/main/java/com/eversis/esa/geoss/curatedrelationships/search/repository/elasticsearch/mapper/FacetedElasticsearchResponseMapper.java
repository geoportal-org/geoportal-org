package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;

import org.elasticsearch.action.search.SearchResponse;

public interface FacetedElasticsearchResponseMapper<T> extends ElasticsearchResponseMapper<T> {

    /**
     * Wrap elasticsearch response to custom page object including facets.
     *
     * @param searchResponse Elasticsearch response
     * @param pageable pagination information
     */
    FacetedPage<T> mapFacetedSearchResponse(SearchResponse searchResponse, Pageable pageable);

}
