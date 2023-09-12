package com.eversis.esa.geoss.curated.common.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type Common configuration.
 */
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.curated.common.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.common.email",
                "com.eversis.esa.geoss.curated.common.controller",
                "com.eversis.esa.geoss.curated.common.service.impl",
                "com.eversis.esa.geoss.curated.common.mapper",
        }
)
@Configuration(proxyBeanMethods = false)
public class CommonConfiguration {

}
