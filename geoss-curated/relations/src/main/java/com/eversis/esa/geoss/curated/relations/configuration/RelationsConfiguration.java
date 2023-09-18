package com.eversis.esa.geoss.curated.relations.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type Relations configuration.
 */
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.curated.relations.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.relations.controller",
                "com.eversis.esa.geoss.curated.relations.service.impl",
                "com.eversis.esa.geoss.curated.relations.mapper",
        }
)
@PropertySource("classpath:application-relations.properties")
@Configuration(proxyBeanMethods = false)
public class RelationsConfiguration {

}
