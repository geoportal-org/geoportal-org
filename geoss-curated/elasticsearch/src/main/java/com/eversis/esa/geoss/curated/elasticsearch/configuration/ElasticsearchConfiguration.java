package com.eversis.esa.geoss.curated.elasticsearch.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import jakarta.annotation.PostConstruct;

/**
 * The type Elasticsearch configuration.
 */
@Log4j2
@EnableElasticsearchRepositories(basePackages = "com.eversis.esa.geoss.curated.elasticsearch.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.elasticsearch.mapper",
                "com.eversis.esa.geoss.curated.elasticsearch.service.impl",
        }
)
@PropertySource("classpath:application-elasticsearch.properties")
@Configuration(proxyBeanMethods = false)
public class ElasticsearchConfiguration {

    @PostConstruct
    void init() {
        log.debug("Init module");
    }
}
