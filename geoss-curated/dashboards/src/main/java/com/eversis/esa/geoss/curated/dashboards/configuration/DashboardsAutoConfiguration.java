package com.eversis.esa.geoss.curated.dashboards.configuration;

import com.eversis.esa.geoss.curated.dashboards.properties.DashboardsProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Dashboards auto configuration.
 */
@AutoConfiguration
@ConditionalOnProperty(
        prefix = "geoss.curated.dashboards", name = "enabled", havingValue = "true", matchIfMissing = true
)
@EnableConfigurationProperties(
        value = {
                DashboardsProperties.class
        }
)
@Import(DashboardsConfiguration.class)
public class DashboardsAutoConfiguration {

}
