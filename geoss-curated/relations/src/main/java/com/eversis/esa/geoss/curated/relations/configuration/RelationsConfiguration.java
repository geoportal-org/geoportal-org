package com.eversis.esa.geoss.curated.relations.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type Relations configuration.
 */
@EnableElasticsearchRepositories(basePackages = "com.eversis.esa.geoss.curated.relations.elasticsearch.repository")
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.curated.relations.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.relations.controller",
                "com.eversis.esa.geoss.curated.relations.service.impl",
                "com.eversis.esa.geoss.curated.relations.mapper",
                "com.eversis.esa.geoss.curated.relations.elasticsearch.service.impl",
                "com.eversis.esa.geoss.curated.relations.elasticsearch.mapper"
        }
)
@PropertySource("classpath:application-relations.properties")
@Configuration(proxyBeanMethods = false)
public class RelationsConfiguration {

}
