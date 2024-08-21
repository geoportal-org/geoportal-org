package com.eversis.esa.geoss.curatedrelationships.search.config.datasource;

import lombok.extern.log4j.Log4j2;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The type Elasticsearch config.
 */
@Log4j2
@Configuration
public class ElasticsearchConfig {

    @Value("${datasource.elasticsearch.compatibility-mode:true}")
    private boolean elasticsearchCompatibilityMode;

    /**
     * Elasticsearch client rest high level client.
     *
     * @param restClient the rest client
     * @return the rest high level client
     */
    @Bean(destroyMethod = "close")
    RestHighLevelClient restHighLevelClient(RestClient restClient) {
        return new RestHighLevelClientBuilder(restClient)
                .setApiCompatibilityMode(elasticsearchCompatibilityMode)
                .build();
    }
}
