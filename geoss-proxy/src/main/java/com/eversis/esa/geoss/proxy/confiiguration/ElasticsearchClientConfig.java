package com.eversis.esa.geoss.proxy.confiiguration;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

/**
 * The type Elasticsearch client config.
 */
@Slf4j
@Configuration
public class ElasticsearchClientConfig extends ElasticsearchConfiguration {

    /**
     * The Connection url.
     */
    @Value("${spring.elasticsearch.rest.uris}")
    String connectionUrl;

    @Override
    public ClientConfiguration clientConfiguration() {
        log.info("Configuring Elasticsearch Client host: {}", connectionUrl);
        try {
            log.info("Waiting for Elasticsearch docker initialization...");
            TimeUnit.SECONDS.sleep(30);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return ClientConfiguration.builder()
                .connectedTo(connectionUrl)
                .build();
    }

}
