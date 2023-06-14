package com.eversis.esa.geoss.curated.relations.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Relations configuration.
 */
@ComponentScan(
        basePackages = {
        }
)
@PropertySource("classpath:application-relations.properties")
@Configuration(proxyBeanMethods = false)
public class RelationsConfiguration {

}
