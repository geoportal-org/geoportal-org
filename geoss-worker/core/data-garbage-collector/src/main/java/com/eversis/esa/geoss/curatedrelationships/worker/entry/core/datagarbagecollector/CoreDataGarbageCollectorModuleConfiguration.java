package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * The type Core data garbage collector module configuration.
 */
@Log4j2
@ComponentScan(basePackages = "com.eversis.esa.geoss.curatedrelationships.worker.entry.core.datagarbagecollector")
@Configuration
class CoreDataGarbageCollectorModuleConfiguration {

    /**
     * Post construct.
     */
    @PostConstruct
    public void postConstruct() {
        log.info("Module: core-data-garbage-collector has been successfully loaded");
    }

}
