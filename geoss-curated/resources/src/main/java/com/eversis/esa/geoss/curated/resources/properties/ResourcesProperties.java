package com.eversis.esa.geoss.curated.resources.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Resources properties.
 */
@Getter
@Setter
@ConfigurationProperties("geoss.curated.resources")
public class ResourcesProperties {

    private boolean enabled;
}
