package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl.aggregation;

import com.eversis.esa.geoss.curatedrelationships.search.model.Facets;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Facet;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.SimpleTermFacet;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class AggregationsMapper {

    private final SourceAggregationMapper sourceAggregationMapper;

    @Autowired
    public AggregationsMapper(SourceAggregationMapper sourceAggregationMapper) {
        this.sourceAggregationMapper = sourceAggregationMapper;
    }

    public Map<String, List<Facet>> collectFacets(Aggregations aggregations) {
        Map<String, List<Facet>> facets = new HashMap<>();
        facets.put(Facets.ORGANISATION.getName(), collectStringFacets(aggregations, Facets.ORGANISATION.getName()));
        facets.put(Facets.PROTOCOL.getName(), collectStringFacets(aggregations, Facets.PROTOCOL.getName()));
        facets.put(Facets.KEYWORDS.getName(), collectStringFacets(aggregations, Facets.KEYWORDS.getName()));
        facets.put(Facets.SOURCE.getName(), sourceAggregationMapper.collectSourceFacetsFromStringFacets(collectStringFacets(aggregations, Facets.SOURCE.getName())));
        return facets;
    }

    private List<Facet> collectStringFacets(Aggregations aggregations, String field) {
        Terms termsAggregation = aggregations.get(field);

        if (termsAggregation == null) {
            return Collections.emptyList();
        }
        return termsAggregation.getBuckets()
                .stream()
                .map(this::createFacet)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private Optional<Facet> createFacet(Bucket bucket) {
        try {
            return Optional.of(new SimpleTermFacet(bucket.getKeyAsString(), bucket.getDocCount()));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }

}
