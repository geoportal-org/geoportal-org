package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Facet;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.FacetedPageImpl;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchDocumentMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.FacetedElasticsearchResponseMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl.aggregation.AggregationsMapper;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EntryResponseMapper extends BaseElasticsearchResponseMapper<Entry> implements FacetedElasticsearchResponseMapper<Entry> {

    private final AggregationsMapper aggregationsMapper;

    public EntryResponseMapper(ElasticsearchDocumentMapper<Entry> documentMapper, AggregationsMapper aggregationsMapper) {
        super(documentMapper);
        this.aggregationsMapper = aggregationsMapper;
    }

    @Override
    public FacetedPage<Entry> mapFacetedSearchResponse(
            SearchResponse searchResponse,
            Pageable pageable) {
        Page<Entry> results = super.mapSearchResponse(searchResponse, pageable);
        Map<String, List<Facet>> facets = aggregationsMapper.collectFacets(searchResponse.getAggregations());
        return new FacetedPageImpl<>(results.getContent(), pageable, results.getTotalElements(), facets);
    }
}
