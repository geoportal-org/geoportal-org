package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import jakarta.annotation.PostConstruct;

/**
 * The type Core data module configuration.
 */
@Log4j2
@Configuration
@ComponentScan(basePackages = "com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data")
@EntityScan(basePackages = "com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model")
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.repository")
@EnableJpaAuditing
class CoreDataModuleConfiguration {

    /**
     * Post construct.
     */
    @PostConstruct
    public void postConstruct() {
        log.info("Module: core-data has been successfully loaded");
    }
}
