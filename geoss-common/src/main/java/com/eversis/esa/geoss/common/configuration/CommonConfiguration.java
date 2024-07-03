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
                "com.eversis.esa.geoss.common.hateoas",
                "com.eversis.esa.geoss.common.rest",
                "com.eversis.esa.geoss.common.security.configuration",
                "com.eversis.esa.geoss.common.security.oauth2.configuration"
        }
)
@Configuration(proxyBeanMethods = false)
public class CommonConfiguration {

}
