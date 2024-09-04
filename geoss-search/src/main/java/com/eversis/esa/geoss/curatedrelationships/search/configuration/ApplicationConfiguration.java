package com.eversis.esa.geoss.curatedrelationships.search.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * The type Application configuration.
 */
@Log4j2
@EnableAsync
@EnableScheduling
@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {

}
