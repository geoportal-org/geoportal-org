package com.eversis.esa.geoss.curated.elasticsearch.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

/**
 * The type Elasticsearch configuration.
 */
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

}
