package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.PageRequest;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Concept;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ThesaurusRepository;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.config.ElasticsearchConfigurationProperties;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchResponseMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.query.ConceptQueryFactory;

import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
public class ConceptElasticsearchRepository extends ElasticsearchRepository<Concept> implements ThesaurusRepository {

    private final ConceptQueryFactory queryFactory;
    private final ElasticsearchConfigurationProperties elasticsearchConfigurationProperties;

    public ConceptElasticsearchRepository(RestHighLevelClient client,
            ElasticsearchResponseMapper<Concept> responseMapper,
            ConceptQueryFactory queryFactory,
            ElasticsearchConfigurationProperties elasticsearchConfigurationProperties) {
        super(client, responseMapper);
        this.queryFactory = queryFactory;
        this.elasticsearchConfigurationProperties = elasticsearchConfigurationProperties;
    }

    @Override
    public Page<Concept> findConcepts(String searchPhrase, Pageable pageable) {
        QueryBuilder mainQueryBuilder = queryFactory.buildSearchQuery(searchPhrase);
        Pageable queryPageable = new PageRequest(pageable.getStartIndex(), pageable.getPageSize());
        return search(queryPageable, mainQueryBuilder);
    }

    @Override
    String indexName() {
        return elasticsearchConfigurationProperties.getThesaurusIndex();
    }

    @Override
    String indexType() {
        return elasticsearchConfigurationProperties.getThesaurusType();
    }

}
