package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch;

import com.eversis.esa.geoss.curatedrelationships.search.model.Facets;
import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQuery;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.FacetedPage;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.PageRequest;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Entry;
import com.eversis.esa.geoss.curatedrelationships.search.repository.CRRepository;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.config.ElasticsearchConfigurationProperties;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.FacetedElasticsearchResponseMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.query.EntryAggregationFactory;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.query.EntryQueryFactory;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.KEYWORDS_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.ORGANIZATION_TITLE_RAW_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.SCORE_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.SOURCE_ID_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.SOURCE_TERM_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TITLE_RAW_FIELD;
import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr.GeossCrEntryElasticsearchFields.TRANSFER_OPTIONS_PROTOCOL_FIELD;

/**
 * The type Entry elasticsearch repository.
 */
@Repository("geossRepository")
public class EntryElasticsearchRepository extends FacetedElasticsearchRepository<Entry> implements CRRepository<Entry> {

    private static final Map<Facets, List<String>> FACET_FIELDS;

    static {
        FACET_FIELDS = new HashMap<>();
        FACET_FIELDS.put(Facets.SOURCE, Arrays.asList(SOURCE_ID_FIELD, SOURCE_TERM_FIELD));
        FACET_FIELDS.put(Facets.ORGANISATION, Collections.singletonList(ORGANIZATION_TITLE_RAW_FIELD));
        FACET_FIELDS.put(Facets.PROTOCOL, Collections.singletonList(TRANSFER_OPTIONS_PROTOCOL_FIELD));
        FACET_FIELDS.put(Facets.KEYWORDS, Collections.singletonList(KEYWORDS_FIELD));
    }

    private final EntryQueryFactory queryFactory;
    private final EntryAggregationFactory aggregationFactory;
    private final ElasticsearchConfigurationProperties elasticsearchConfigurationProperties;

    /**
     * Instantiates a new Entry elasticsearch repository.
     *
     * @param client the client
     * @param elasticsearchConfigurationProperties the elasticsearch configuration properties
     * @param searchResponseMapper the search response mapper
     * @param queryFactory the query factory
     * @param aggregationFactory the aggregation factory
     */
    @Autowired
    public EntryElasticsearchRepository(
            RestHighLevelClient client,
            ElasticsearchConfigurationProperties elasticsearchConfigurationProperties,
            FacetedElasticsearchResponseMapper<Entry> searchResponseMapper,
            EntryQueryFactory queryFactory,
            EntryAggregationFactory aggregationFactory) {
        super(client, searchResponseMapper);
        this.elasticsearchConfigurationProperties = elasticsearchConfigurationProperties;
        this.queryFactory = queryFactory;
        this.aggregationFactory = aggregationFactory;
    }

    @Override
    public Page<Entry> findResourcesById(Set<String> ids, Pageable pageable) {
        return search(pageable, queryFactory.buildIdsQuery(ids));
    }

    @Override
    public FacetedPage<Entry> findResources(SearchQuery searchParameters, Pageable pageable) {
        QueryBuilder mainQueryBuilder = queryFactory.buildSearchQuery(searchParameters);
        List<AggregationBuilder> aggregations = aggregationFactory.buildAggregations(FACET_FIELDS);
        Sort sort = getSortOrdering();
        if (pageable.getSort() != null) {
            sort = sort.and(pageable.getSort());
        }
        Pageable queryPageable = new PageRequest(pageable.getStartIndex(), pageable.getPageSize(), sort);
        return search(queryPageable, mainQueryBuilder, aggregations);
    }

    private Sort getSortOrdering() {
        Sort.Order scoreOrder = new Sort.Order(Sort.Direction.DESC, SCORE_FIELD);
        Sort.Order titleOrder = new Order(Sort.Direction.DESC, TITLE_RAW_FIELD);

        return Sort.by(scoreOrder, titleOrder);
    }

    @Override
    String indexName() {
        return elasticsearchConfigurationProperties.getGeossIndex();
    }

    @Override
    String indexType() {
        return elasticsearchConfigurationProperties.getGeossType();
    }
}
