package com.eversis.esa.geoss.curated.common.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The type Common configuration.
 */

@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.common.email"
        }
)
@Configuration(proxyBeanMethods = false)
public class CommonConfiguration {

}
