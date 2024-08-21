package com.eversis.esa.geoss.proxy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * The type Geoss proxy application test.
 */
@SpringBootTest
@Testcontainers
class GeossProxyApplicationTest {

    /**
     * The constant ELASTICSEARCH_CONTAINER.
     */
    @Container
    @ServiceConnection
    static ElasticsearchContainer ELASTICSEARCH_CONTAINER = new ElasticsearchContainer(
            "docker.elastic.co/elasticsearch/elasticsearch:8.9.0")
            .withEnv("xpack.security.enabled", "false");

    /**
     * Properties.
     *
     * @param registry the registry
     */
    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.elasticsearch.uris", () -> "http://" + ELASTICSEARCH_CONTAINER.getHttpHostAddress());
    }

    /**
     * Context loads.
     */
    @Test
    void contextLoads() {
    }
}
