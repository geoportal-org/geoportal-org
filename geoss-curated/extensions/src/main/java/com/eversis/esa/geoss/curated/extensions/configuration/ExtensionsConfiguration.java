package com.eversis.esa.geoss.curated.extensions.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.annotation.PostConstruct;

/**
 * The type Extensions configuration.
 */
@Log4j2
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.curated.extensions.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.extensions.controller",
                "com.eversis.esa.geoss.curated.extensions.service.impl",
                "com.eversis.esa.geoss.curated.extensions.mapper",
        }
)
@PropertySource("classpath:application-extensions.properties")
@Configuration(proxyBeanMethods = false)
public class ExtensionsConfiguration {

    @PostConstruct
    void init() {
        log.warn("Init module");
    }
}
