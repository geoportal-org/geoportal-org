package com.eversis.esa.geoss.curated.dashboards.configuration;

import com.eversis.esa.geoss.curated.dashboards.properties.DashboardsProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Dashboards auto configuration.
 */
@AutoConfiguration
@ConditionalOnProperty(
        prefix = "geoss.curated.dashboards", name = "enabled", havingValue = "true"
)
@EnableConfigurationProperties(
        value = {
                DashboardsProperties.class
        }
)
@EntityScan(basePackages = "com.eversis.esa.geoss.curated.dashboards.domain")
@Import(DashboardsConfiguration.class)
public class DashboardsAutoConfiguration {

}
