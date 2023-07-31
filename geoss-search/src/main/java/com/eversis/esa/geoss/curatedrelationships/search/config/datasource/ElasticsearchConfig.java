package com.eversis.esa.geoss.curatedrelationships.search.config.datasource;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * The type Elasticsearch config.
 */
@Slf4j
@Configuration
public class ElasticsearchConfig {

    @Value("${datasource.elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${datasource.elasticsearch.port}")
    private int elasticsearchPort;

    /**
     * Elasticsearch client rest high level client.
     *
     * @return the rest high level client
     */
    @Primary
    @Bean(destroyMethod = "close")
    public RestHighLevelClient elasticsearchClient() {
        log.info("Configuring Elasticsearch RestHighLevelClient host: {}, port: {}", elasticsearchHost,
                elasticsearchPort);
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost(elasticsearchHost, elasticsearchPort)));
    }

}
