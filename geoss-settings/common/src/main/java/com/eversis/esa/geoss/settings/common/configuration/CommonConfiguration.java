package com.eversis.esa.geoss.settings.common.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The type Common configuration.
 */
@EnableConfigurationProperties(
        value = {
                DefaultContactProperties.class,
                EmailProperties.class
        }
)
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.settings.common.hateoas"
        }
)
@Configuration(proxyBeanMethods = false)
public class CommonConfiguration {

}
