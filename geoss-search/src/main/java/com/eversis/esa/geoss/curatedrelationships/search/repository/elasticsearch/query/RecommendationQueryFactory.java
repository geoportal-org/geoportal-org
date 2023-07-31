package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.query;

import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.recommendation.RecommendationElasticsearchFields;

import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.stereotype.Component;

@Component
public class RecommendationQueryFactory {

    public QueryBuilder buildSearchQuery(String searchPhrase) {
        return buildContentQuery(searchPhrase);
    }

    private QueryBuilder buildContentQuery(String searchPhrase) {
        MultiMatchQueryBuilder phraseMultiMatchQueryBuilder = QueryBuilders.multiMatchQuery(searchPhrase);
        phraseMultiMatchQueryBuilder.field(RecommendationElasticsearchFields.TERM_FIELD, 1.0f);
        phraseMultiMatchQueryBuilder.field(RecommendationElasticsearchFields.TERM_RAW_FIELD, 4.0f);
        phraseMultiMatchQueryBuilder.field(RecommendationElasticsearchFields.TERM_ENGLISH_FIELD, 3.0f);
        phraseMultiMatchQueryBuilder.field(RecommendationElasticsearchFields.TERM_TRIGRAM_FIELD, 2.0f);
        return phraseMultiMatchQueryBuilder;
    }

}
