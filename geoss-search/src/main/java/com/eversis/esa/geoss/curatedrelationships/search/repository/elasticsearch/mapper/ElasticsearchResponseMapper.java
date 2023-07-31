package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;

import org.elasticsearch.action.search.SearchResponse;

public interface ElasticsearchResponseMapper<T> {

    /**
     * Wrap elasticsearch response to custom page object.
     *
     * @param searchResponse Elasticsearch response
     * @param pageable pagination information
     */
    Page<T> mapSearchResponse(SearchResponse searchResponse, Pageable pageable);

}
