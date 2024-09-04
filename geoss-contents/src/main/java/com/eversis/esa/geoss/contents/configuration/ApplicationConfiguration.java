package com.eversis.esa.geoss.contents.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * The type Application configuration.
 */
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {

}
