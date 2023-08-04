package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.PageRequest;
import com.eversis.esa.geoss.curatedrelationships.search.model.recommendation.Recommendation;
import com.eversis.esa.geoss.curatedrelationships.search.repository.RecommendationRepository;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.config.ElasticsearchConfigurationProperties;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchResponseMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.query.RecommendationQueryFactory;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * The type Recommendation elasticsearch repository.
 */
@Repository("recommendationRepository")
class RecommendationElasticsearchRepository extends ElasticsearchRepository<Recommendation> implements
        RecommendationRepository {

    private final ElasticsearchConfigurationProperties elasticsearchConfigurationProperties;
    private final RecommendationQueryFactory queryFactory;

    /**
     * Instantiates a new Recommendation elasticsearch repository.
     *
     * @param client the client
     * @param responseMapper the response mapper
     * @param elasticsearchConfigurationProperties the elasticsearch configuration properties
     * @param queryFactory the query factory
     */
    public RecommendationElasticsearchRepository(RestHighLevelClient client,
            ElasticsearchResponseMapper<Recommendation> responseMapper,
            ElasticsearchConfigurationProperties elasticsearchConfigurationProperties,
            RecommendationQueryFactory queryFactory) {
        super(client, responseMapper);
        this.elasticsearchConfigurationProperties = elasticsearchConfigurationProperties;
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Recommendation> findRecommendations(
            @NotBlank String term,
            @NotNull Pageable pageable) {
        QueryBuilder mainQueryBuilder = queryFactory.buildSearchQuery(term);
        Pageable queryPageable = new PageRequest(pageable.getStartIndex(), pageable.getPageSize());
        return search(queryPageable, mainQueryBuilder);
    }

    @Override
    String indexName() {
        return elasticsearchConfigurationProperties.getRecommendationIndex();
    }
}
