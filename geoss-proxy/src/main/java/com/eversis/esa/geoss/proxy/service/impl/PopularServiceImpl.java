package com.eversis.esa.geoss.proxy.service.impl;

import java.util.ArrayList;
import java.util.List;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.eversis.esa.geoss.proxy.document.SearchResultDoc;
import com.eversis.esa.geoss.proxy.domain.PopularWord;
import com.eversis.esa.geoss.proxy.repository.SearchResultRepository;
import com.eversis.esa.geoss.proxy.service.PopularService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

/**
 * The type Popular service.
 */
@Slf4j
@Service
public class PopularServiceImpl implements PopularService {

    private ElasticsearchOperations operations;

    public PopularServiceImpl(ElasticsearchOperations operations) {
        this.operations = operations;
    }

    @Override
    public List<PopularWord> getPopularWords(String query, int limit) {

        Query matchPhrasePrefixQuery = NativeQuery.builder()
                .withAggregation("group_by_ds_st", Aggregation.of(a -> a
                        .terms(ta -> ta.field("ds_st").size(limit))))
                .withQuery(q -> q
                        .matchPhrasePrefix(m -> m
                                .field("ds_st")
                                .query(query)
                                .analyzer("standard")
                                .maxExpansions(50)
                                .slop(3)
                        )
                )
                .build();

        SearchHits<SearchResultDoc> searchHits = operations.search(matchPhrasePrefixQuery, SearchResultDoc.class,
                IndexCoordinates.of("geoss_index"));
        log.info("popularWords elements:{}", searchHits.getTotalHits());

        return new ArrayList<>();
    }

}
