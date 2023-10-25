package com.eversis.esa.geoss.curated.relations.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.annotation.PostConstruct;

/**
 * The type Relations configuration.
 */
@Log4j2
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

    @PostConstruct
    void init() {
        log.debug("Init module");
    }
}
