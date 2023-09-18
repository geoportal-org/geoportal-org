package com.eversis.esa.geoss.curated.extensions.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type Extensions configuration.
 */
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

}
