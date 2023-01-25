package com.eversis.esa.geoss.settings.common.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The type Common configuration.
 */

@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.settings.common.event",
                "com.eversis.esa.geoss.settings.common.hateoas"
        }
)
@Configuration(proxyBeanMethods = false)
public class CommonConfiguration {

}
