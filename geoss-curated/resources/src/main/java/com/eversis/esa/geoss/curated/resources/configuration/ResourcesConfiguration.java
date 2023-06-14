package com.eversis.esa.geoss.curated.resources.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Resources configuration.
 */
@ComponentScan(
        basePackages = {
        }
)
@PropertySource("classpath:application-resources.properties")
@Configuration(proxyBeanMethods = false)
public class ResourcesConfiguration {

}
