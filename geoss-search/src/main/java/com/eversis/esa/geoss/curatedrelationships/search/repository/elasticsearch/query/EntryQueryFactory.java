package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.query;

import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFieldWeights;

import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.common.lucene.search.function.CombineFunction;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.springframework.stereotype.Component;

import jakarta.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.EXTENSIONS_SUMMARY_RAW_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.SUMMARY_RAW_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TAGS_RAW_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TITLE_RAW_FIELD;

/**
 * The type Entry query factory.
 */
@Component
public class EntryQueryFactory {

    private final EntryFilterQueryFactory filterQueryFactory;
    private final EntryFilterFunctionFactory filterFunctionFactory;

    /**
     * Instantiates a new Entry query factory.
     *
     * @param filterQueryFactory the filter query factory
     * @param filterFunctionFactory the filter function factory
     */
    public EntryQueryFactory(
            EntryFilterQueryFactory filterQueryFactory,
            EntryFilterFunctionFactory filterFunctionFactory) {
        this.filterQueryFactory = filterQueryFactory;
        this.filterFunctionFactory = filterFunctionFactory;
    }

    /**
     * Creates Elasticsearch query, which will match only specific document ids.
     *
     * @param ids list of document ids
     * @return the query builder
     */
    public QueryBuilder buildIdsQuery(@NotNull Set<String> ids) {
        return QueryBuilders.idsQuery().addIds(ids.toArray(new String[0]));
    }

    /**
     * Creates Elasticsearch query from provided parameters. It is responsible only for search query, which means that
     * it limits search results by using search phrase and provided filter parameters. It does not handle any kind of
     * pagination and aggregations.
     *
     * @param searchParameters parameters which should be mapped to elasticsearch query
     * @return the query builder
     */
    public QueryBuilder buildSearchQuery(@NotNull SearchQuery searchParameters) {
        BoolQueryBuilder searchQueryBuilder = createSearchQuery(searchParameters);

        if (searchParameters.getOptionalPhrase().isPresent() || !searchParameters.getParents().isEmpty()) {
            return QueryBuilders.functionScoreQuery(searchQueryBuilder,
                            createScoreFilterFunctions(
                                    searchParameters.getOptionalPhrase(), searchParameters.getParents()))
                    .boostMode(CombineFunction.MAX);
        }

        return searchQueryBuilder;
    }

    private BoolQueryBuilder createSearchQuery(SearchQuery searchParams) {
        BoolQueryBuilder mainQueryBuilder = QueryBuilders.boolQuery();
        searchParams.getOptionalPhrase().ifPresent(phrase -> {
            QueryBuilder searchPhraseQuery;

            switch (searchParams.getQueryType()) {
                case KEYWORD:
                    searchPhraseQuery = createContentKeywordQuery(phrase);
                    break;
                case FULL_TEXT:
                default:
                    searchPhraseQuery = createContentFullTextQuery(phrase);
                    break;
            }
            mainQueryBuilder.must(searchPhraseQuery);
        });

        mainQueryBuilder.filter(filterQueryFactory.createDataSourceQuery(searchParams.getDataSource().getName()));
        searchParams.getOptionalDateRange()
                .ifPresent(dateRange -> mainQueryBuilder.filter(filterQueryFactory.createDateQuery(dateRange)));
        if (!searchParams.getEntryTypes().isEmpty()) {
            mainQueryBuilder.filter(filterQueryFactory.createResourceEntryTypeQuery(searchParams.getEntryTypes()));
        }
        if (searchParams.getOptionalBoundingBoxRelation().isPresent() && searchParams.getOptionalBoundingBox()
                .isPresent()) {
            mainQueryBuilder.filter(filterQueryFactory.createGeoShapeQuery(
                    searchParams.getOptionalBoundingBoxRelation().get(),
                    searchParams.getOptionalBoundingBox().get()
            ));
        }
        if (!searchParams.getParents().isEmpty()) {
            mainQueryBuilder.filter(filterQueryFactory.createParentsQuery(searchParams.getParents()));
        }
        if (!searchParams.getSources().isEmpty()) {
            mainQueryBuilder.filter(filterQueryFactory.createSourcesQuery(searchParams.getSources()));
        }

        searchParams.getOptionalOrganizationName()
                .ifPresent(orgName -> mainQueryBuilder.filter(filterQueryFactory.createOrganizationQuery(orgName)));
        searchParams.getOptionalProtocol()
                .ifPresent(protocol -> mainQueryBuilder.filter(filterQueryFactory.createProtocolQuery(protocol)));
        searchParams.getOptionalKeyword()
                .ifPresent(keyword -> mainQueryBuilder.filter(filterQueryFactory.createKeywordQuery(keyword)));

        return mainQueryBuilder;
    }

    private FilterFunctionBuilder[] createScoreFilterFunctions(@NotNull Optional<String> searchPhrase,
            Set<String> parentIds) {
        if (!parentIds.isEmpty()) {
            return filterFunctionFactory.createParentsFilterScoreFunctionBuilder(parentIds);
        }

        if (searchPhrase.isPresent()) {
            return filterFunctionFactory.createPhraseBasedFilterScoreFunctionBuilder(searchPhrase.get());
        }

        return new FilterFunctionBuilder[]{};
    }

    private MultiMatchQueryBuilder createContentFullTextQuery(String phrase) {
        return createMultiMatchQuery(phrase, GeossCrEntryElasticsearchFieldWeights.SEARCH_PHRASE_FIELDS_WEIGHTS);
    }

    private BoolQueryBuilder createContentKeywordQuery(String phrase) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        String wildcardQuery = "*" + QueryParser.escape(phrase).toLowerCase() + "*";
        boolQueryBuilder.should(QueryBuilders.wildcardQuery(TITLE_RAW_FIELD, wildcardQuery));
        boolQueryBuilder.should(QueryBuilders.wildcardQuery(SUMMARY_RAW_FIELD, wildcardQuery));
        boolQueryBuilder.should(QueryBuilders.wildcardQuery(EXTENSIONS_SUMMARY_RAW_FIELD, wildcardQuery));
        boolQueryBuilder.should(QueryBuilders.wildcardQuery(TAGS_RAW_FIELD, wildcardQuery));
        return boolQueryBuilder;
    }

    private MultiMatchQueryBuilder createMultiMatchQuery(String phrase, Map<String, Float> searchFields) {
        MultiMatchQueryBuilder phraseMultiMatchQueryBuilder = QueryBuilders.multiMatchQuery(phrase);
        phraseMultiMatchQueryBuilder.fields(searchFields);
        phraseMultiMatchQueryBuilder.minimumShouldMatch("75%");
        return phraseMultiMatchQueryBuilder;
    }

}
