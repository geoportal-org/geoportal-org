package com.eversis.esa.geoss.curated.common.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.annotation.PostConstruct;

/**
 * The type Common configuration.
 */
@Log4j2
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.curated.common.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.common.email",
                "com.eversis.esa.geoss.curated.common.controller",
                "com.eversis.esa.geoss.curated.common.service.impl",
                "com.eversis.esa.geoss.curated.common.mapper",
        }
)
@Configuration(proxyBeanMethods = false)
public class CommonConfiguration {

    @PostConstruct
    void init() {
        log.debug("Init module");
    }
}
