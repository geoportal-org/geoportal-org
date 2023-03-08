package com.eversis.esa.geoss.userdata.common.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The type Common configuration.
 */

@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.userdata.common.event",
                "com.eversis.esa.geoss.userdata.common.hateoas"
        }
)
@Configuration(proxyBeanMethods = false)
public class CommonConfiguration {

}
