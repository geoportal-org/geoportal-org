package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.query;

import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.thesaurus.ThesaurusElasticsearchFields;

import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction.Modifier;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;
import org.springframework.stereotype.Component;

import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.thesaurus.ThesaurusElasticsearchFields.WEIGHT;

@Component
public class ConceptQueryFactory {

    public QueryBuilder buildSearchQuery(String searchPhrase) {
        QueryBuilder contentQuery = buildContentQuery(searchPhrase);
        return QueryBuilders.functionScoreQuery(
                contentQuery,
                createFieldValueScoreFunctionBuilder(contentQuery))
                .boostMode(CombineFunction.MULTIPLY);
    }

    private QueryBuilder buildContentQuery(String searchPhrase) {
        MultiMatchQueryBuilder phraseMultiMatchQueryBuilder = QueryBuilders.multiMatchQuery(searchPhrase);
        phraseMultiMatchQueryBuilder.field(ThesaurusElasticsearchFields.TERM_FIELD, 1.0f);
        phraseMultiMatchQueryBuilder.field(ThesaurusElasticsearchFields.TERM_RAW_FIELD, 4.0f);
        phraseMultiMatchQueryBuilder.field(ThesaurusElasticsearchFields.TERM_ENGLISH_FIELD, 3.0f);
        phraseMultiMatchQueryBuilder.field(ThesaurusElasticsearchFields.TERM_TRIGRAM_FIELD, 2.0f);
        phraseMultiMatchQueryBuilder.field(ThesaurusElasticsearchFields.DEFINITION_FIELD, 1.0f);
        return phraseMultiMatchQueryBuilder;
    }

    private FilterFunctionBuilder[] createFieldValueScoreFunctionBuilder(QueryBuilder queryBuilder) {
        ScoreFunctionBuilder scoreFunctionBuilder = ScoreFunctionBuilders.fieldValueFactorFunction(WEIGHT).missing(1.0).modifier(Modifier.SQRT);
        return new FilterFunctionBuilder[]{new FilterFunctionBuilder(queryBuilder, scoreFunctionBuilder)};
    }

}
