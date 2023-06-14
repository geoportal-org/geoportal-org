package com.eversis.esa.geoss.curated.dashboards.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Dashboards configuration.
 */
@ComponentScan(
        basePackages = {
        }
)
@PropertySource("classpath:application-dashboards.properties")
@Configuration(proxyBeanMethods = false)
public class DashboardsConfiguration {

}
