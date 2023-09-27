package com.eversis.esa.geoss.curatedrelationships.common.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The type Common configuration.
 */

@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curatedrelationships.common.configuration.boot",
                "com.eversis.esa.geoss.curatedrelationships.common.configuration.security"
        }
)
@Configuration(proxyBeanMethods = false)
public class CommonConfiguration {

}
