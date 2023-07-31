package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.query;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermsQueryBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.PARENT_ID_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.SCORE_WEIGHT_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TAGS_RAW_FIELD;

@Component
public class EntryFilterFunctionFactory {

    FilterFunctionBuilder[] createPhraseBasedFilterScoreFunctionBuilder(String searchPhrase) {
        TermsQueryBuilder queryBuilder = QueryBuilders.termsQuery(TAGS_RAW_FIELD, searchPhrase);
        return createFieldValueScoreFunctionBuilder(queryBuilder);
    }

    FilterFunctionBuilder[] createParentsFilterScoreFunctionBuilder(Set<String> parentIds) {
        TermsQueryBuilder queryBuilder = QueryBuilders.termsQuery(PARENT_ID_FIELD, parentIds);
        return createFieldValueScoreFunctionBuilder(queryBuilder);
    }

    private FilterFunctionBuilder[] createFieldValueScoreFunctionBuilder(QueryBuilder queryBuilder) {
        ScoreFunctionBuilder scoreFunctionBuilder = ScoreFunctionBuilders.fieldValueFactorFunction(SCORE_WEIGHT_FIELD).missing(1.0);
        return new FilterFunctionBuilder[]{new FilterFunctionBuilder(queryBuilder, scoreFunctionBuilder)};
    }

}
