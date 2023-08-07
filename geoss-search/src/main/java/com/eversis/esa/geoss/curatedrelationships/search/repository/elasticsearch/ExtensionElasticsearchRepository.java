package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.entity.Extension;
import com.eversis.esa.geoss.curatedrelationships.search.repository.ExtensionRepository;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.config.ElasticsearchConfigurationProperties;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchResponseMapper;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.query.ExtensionQueryFactory;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Repository;

import jakarta.validation.constraints.NotNull;
import java.util.Set;

/**
 * The type Extension elasticsearch repository.
 */
@Repository
public class ExtensionElasticsearchRepository extends ElasticsearchRepository<Extension> implements
        ExtensionRepository {

    private final ExtensionQueryFactory queryFactory;
    private final ElasticsearchConfigurationProperties elasticsearchConfigurationProperties;

    /**
     * Instantiates a new Extension elasticsearch repository.
     *
     * @param client the client
     * @param responseMapper the response mapper
     * @param queryFactory the query factory
     * @param elasticsearchConfigurationProperties the elasticsearch configuration properties
     */
    public ExtensionElasticsearchRepository(RestHighLevelClient client,
            ElasticsearchResponseMapper<Extension> responseMapper,
            ExtensionQueryFactory queryFactory,
            ElasticsearchConfigurationProperties elasticsearchConfigurationProperties) {
        super(client, responseMapper);
        this.queryFactory = queryFactory;
        this.elasticsearchConfigurationProperties = elasticsearchConfigurationProperties;
    }

    @Override
    public Page<Extension> findExtensions(
            @NotNull Set<String> ids,
            @NotNull DataSource dataSource,
            @NotNull Pageable pageable) {
        return search(pageable, queryFactory.buildIdsQuery(ids, dataSource.getName()));
    }

    @Override
    String indexName() {
        return elasticsearchConfigurationProperties.getExtensionsIndex();
    }
}
