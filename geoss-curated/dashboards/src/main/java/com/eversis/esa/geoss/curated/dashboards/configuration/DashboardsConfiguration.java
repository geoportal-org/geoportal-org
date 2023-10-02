package com.eversis.esa.geoss.curated.dashboards.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type Dashboards configuration.
 */
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.curated.dashboards.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.dashboards.controller",
                "com.eversis.esa.geoss.curated.dashboards.service.impl",
                "com.eversis.esa.geoss.curated.dashboards.mapper"
        }
)
@PropertySource("classpath:application-dashboards.properties")
@Configuration(proxyBeanMethods = false)
public class DashboardsConfiguration {

}
