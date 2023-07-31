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

/**
 * The type Entry filter function factory.
 */
@Component
public class EntryFilterFunctionFactory {

    /**
     * Create phrase based filter score function builder filter function builder [ ].
     *
     * @param searchPhrase the search phrase
     * @return the filter function builder [ ]
     */
    FilterFunctionBuilder[] createPhraseBasedFilterScoreFunctionBuilder(String searchPhrase) {
        TermsQueryBuilder queryBuilder = QueryBuilders.termsQuery(TAGS_RAW_FIELD, searchPhrase);
        return createFieldValueScoreFunctionBuilder(queryBuilder);
    }

    /**
     * Create parents filter score function builder filter function builder [ ].
     *
     * @param parentIds the parent ids
     * @return the filter function builder [ ]
     */
    FilterFunctionBuilder[] createParentsFilterScoreFunctionBuilder(Set<String> parentIds) {
        TermsQueryBuilder queryBuilder = QueryBuilders.termsQuery(PARENT_ID_FIELD, parentIds);
        return createFieldValueScoreFunctionBuilder(queryBuilder);
    }

    private FilterFunctionBuilder[] createFieldValueScoreFunctionBuilder(QueryBuilder queryBuilder) {
        ScoreFunctionBuilder scoreFunctionBuilder = ScoreFunctionBuilders.fieldValueFactorFunction(SCORE_WEIGHT_FIELD)
                .missing(1.0);
        return new FilterFunctionBuilder[]{new FilterFunctionBuilder(queryBuilder, scoreFunctionBuilder)};
    }

}
