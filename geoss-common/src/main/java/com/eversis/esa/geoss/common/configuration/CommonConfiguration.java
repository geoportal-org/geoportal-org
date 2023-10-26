package com.eversis.esa.geoss.common.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The type Common configuration.
 */

@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.common.boot",
                "com.eversis.esa.geoss.common.controller",
                "com.eversis.esa.geoss.common.event",
                "com.eversis.esa.geoss.common.exception",
                "com.eversis.esa.geoss.common.hateoas"
        }
)
@Configuration(proxyBeanMethods = false)
public class CommonConfiguration {

}
