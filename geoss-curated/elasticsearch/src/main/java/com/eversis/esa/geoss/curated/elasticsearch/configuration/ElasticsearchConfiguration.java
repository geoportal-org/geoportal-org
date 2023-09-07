package com.eversis.esa.geoss.curated.elasticsearch.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Elasticsearch configuration.
 */
@ComponentScan(
        basePackages = {
        }
)
@PropertySource("classpath:application-elasticsearch.properties")
@Configuration(proxyBeanMethods = false)
public class ElasticsearchConfiguration {

}
