package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.FacetedElasticsearchResponseMapper;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.util.List;

abstract class FacetedElasticsearchRepository<T> extends ElasticsearchRepository<T> {

    private final FacetedElasticsearchResponseMapper<T> responseMapper;

    public FacetedElasticsearchRepository(
            RestHighLevelClient client,
            FacetedElasticsearchResponseMapper<T> responseMapper) {
        super(client, responseMapper);
        this.responseMapper = responseMapper;
    }

    public FacetedPage<T> search(
            Pageable pageable,
            QueryBuilder query,
            List<AggregationBuilder> aggregations) {
        SearchSourceBuilder sourceBuilder = getSearchSourceBuilder(pageable, query, aggregations);
        SearchResponse response = search(sourceBuilder);
        return responseMapper.mapFacetedSearchResponse(response, pageable);
    }

    private SearchSourceBuilder getSearchSourceBuilder(Pageable pageable, QueryBuilder query, List<AggregationBuilder> aggregations) {
        SearchSourceBuilder sourceBuilder = getPaginatedSearchSourceBuilder(pageable);
        sourceBuilder.query(query);
        aggregations.forEach(sourceBuilder::aggregation);
        return sourceBuilder;
    }

}
