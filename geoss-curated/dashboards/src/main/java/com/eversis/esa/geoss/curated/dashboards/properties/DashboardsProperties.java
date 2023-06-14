package com.eversis.esa.geoss.curated.dashboards.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Dashboards properties.
 */
@Getter
@Setter
@ConfigurationProperties("geoss.curated.dashboards")
public class DashboardsProperties {

    private boolean enabled;
}
