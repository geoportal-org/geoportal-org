package com.eversis.esa.geoss.curatedrelationships.search.config.datasource;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpHost;
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

    @Value("${datasource.elasticsearch.host}")
    private String elasticsearchHost;

    @Value("${datasource.elasticsearch.port}")
    private int elasticsearchPort;

    @Value("${datasource.elasticsearch.compatibilityMode}")
    private boolean elasticsearchCompatibilityMode;

    /**
     * Rest client.
     *
     * @return the rest client
     */
    @Bean(destroyMethod = "close")
    RestClient restClient() {
        log.info("Configuring Elasticsearch RestClient host: {}, port: {}",
                elasticsearchHost, elasticsearchPort);
        return RestClient.builder(new HttpHost(elasticsearchHost, elasticsearchPort)).build();
    }

    /**
     * Elasticsearch client rest high level client.
     *
     * @param restClient the rest client
     * @return the rest high level client
     */
    @Bean(destroyMethod = "close")
    public RestHighLevelClient restHighLevelClient(RestClient restClient) {
        return new RestHighLevelClientBuilder(restClient)
                .setApiCompatibilityMode(elasticsearchCompatibilityMode)
                .build();
    }

    /**
     * Elasticsearch client.
     *
     * @param restClient the rest client
     * @return the elasticsearch client
     */
    @Bean
    public ElasticsearchClient elasticsearchClient(RestClient restClient) {
        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );
        return new ElasticsearchClient(transport);
    }
}
