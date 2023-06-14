package com.eversis.esa.geoss.curated.extensions.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Extensions configuration.
 */
@ComponentScan(
        basePackages = {
        }
)
@PropertySource("classpath:application-extensions.properties")
@Configuration(proxyBeanMethods = false)
public class ExtensionsConfiguration {

}
