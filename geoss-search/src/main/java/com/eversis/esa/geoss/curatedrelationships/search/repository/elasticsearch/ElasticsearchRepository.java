package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchResponseMapper;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;
import java.util.stream.StreamSupport;

@Slf4j
abstract class ElasticsearchRepository<T> {

    private final RestHighLevelClient client;
    private final ElasticsearchResponseMapper<T> responseMapper;

    abstract String indexName();
    abstract String indexType();

    public ElasticsearchRepository(RestHighLevelClient client,
            ElasticsearchResponseMapper<T> responseMapper) {
        this.client = client;
        this.responseMapper = responseMapper;
    }

    public Page<T> search(Pageable pageable, QueryBuilder query) {
        SearchSourceBuilder sourceBuilder = getSearchSourceBuilder(pageable, query);
        SearchResponse response = search(sourceBuilder);
        return responseMapper.mapSearchResponse(response, pageable);
    }

    protected SearchResponse search(SearchSourceBuilder sourceBuilder) {
        SearchRequest searchRequest = new SearchRequest(indexName());
        searchRequest.types(indexType());
        searchRequest.source(sourceBuilder);
        return search(searchRequest);
    }

    private SearchResponse search(SearchRequest searchRequest) {
        try {
            if (log.isTraceEnabled()) {
                log.trace("Elasticsearch Query : {}", searchRequest);
            }
            SearchResponse response = client.search(searchRequest, RequestOptions.DEFAULT);
            if (log.isTraceEnabled()) {
                log.trace("Elasticsearch Response : {}", response);
            }
            return response;
        } catch (IOException ex) {
            log.error("Error occurred during execution of Search request [{}] : {}", searchRequest, ex);
            throw new ElasticsearchException(ex);
        }
    }

    private SearchSourceBuilder getSearchSourceBuilder(Pageable pageable, QueryBuilder query) {
        SearchSourceBuilder sourceBuilder = getPaginatedSearchSourceBuilder(pageable);
        sourceBuilder.query(query);
        return sourceBuilder;
    }

    protected SearchSourceBuilder getPaginatedSearchSourceBuilder(Pageable pageable) {
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder()
                .from(pageable.getStartIndex())
                .size(pageable.getPageSize());
        if (pageable.getSort() != null) {
            StreamSupport.stream(pageable.getSort().spliterator(), false)
                    .forEach(order -> sourceBuilder
                            .sort(order.getProperty(), SortOrder.fromString(order.getDirection().name())));
        }

        return sourceBuilder;
    }

}
