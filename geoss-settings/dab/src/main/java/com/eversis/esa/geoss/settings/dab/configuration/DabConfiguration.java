package com.eversis.esa.geoss.settings.dab.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The type  dab configuration.
 */
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.settings.dab.controller",
                "com.eversis.esa.geoss.settings.dab.service.internal",
        }
)
@PropertySource("classpath:application-dab.properties")
@Configuration(proxyBeanMethods = false)
public class DabConfiguration {

}
