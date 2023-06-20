package com.eversis.esa.geoss.proxy.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.eversis.esa.geoss.proxy.domain.PopularWord;
import com.eversis.esa.geoss.proxy.service.PopularService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * The type Popular service.
 */
@Slf4j
@Service
public class PopularServiceImpl implements PopularService {

    public static final String FIELD_NAME = "ds_st";
    public static final String AGGREGATION_NAME = "group_by_ds_st";
    public static final String INDEX_NAME = "geoss_index";
    private final ElasticsearchClient elasticsearchClient;

    public PopularServiceImpl(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    @Override
    public List<PopularWord> getPopularWords(String queryStr, int limit) {
        return extractPopularWords(queryStr, limit);
    }

    private List<PopularWord> extractPopularWords(String queryStr, int limit) {
        List<PopularWord> popularWords = new ArrayList<>();
        SearchResponse<Void> response;
        try {
            response = elasticsearchClient.search(b -> b
                            .index(INDEX_NAME)
                            .size(0)
                            .query(q -> q
                                    .matchPhrasePrefix(m -> m
                                            .field(FIELD_NAME)
                                            .query(queryStr)))
                            .aggregations(AGGREGATION_NAME, a -> a
                                    .terms(ta -> ta.field(FIELD_NAME).size(limit))
                            ),
                    Void.class
            );
            getPopularWordsFromBuckets(popularWords, response);
        } catch (IOException e) {
            throw new RuntimeException("Could not get popular words. Error: " + e.getMessage());
        }
        return popularWords;
    }

    private void getPopularWordsFromBuckets(List<PopularWord> popularWords, SearchResponse<Void> response) {
        Map<String, Aggregate> aggregations = response.aggregations();
        for (Map.Entry<String, Aggregate> entry : aggregations.entrySet()) {
            Aggregate aggregate = entry.getValue();
            StringTermsAggregate sterms = aggregate.sterms();
            Buckets<StringTermsBucket> sbuckets = sterms.buckets();
            List<StringTermsBucket> bucArr = sbuckets.array();
            for (StringTermsBucket bucObj : bucArr) {
                     popularWords.add(new PopularWord(bucObj.key().stringValue(), bucObj.docCount()));
            }
        }
    }

}
