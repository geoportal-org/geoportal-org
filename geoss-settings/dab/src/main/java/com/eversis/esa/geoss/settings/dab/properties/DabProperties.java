package com.eversis.esa.geoss.settings.dab.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Dab properties.
 */
@Getter
@Setter
@ConfigurationProperties("geoss.settings.dab")
public class DabProperties {

    private boolean enabled;

    private long connectTimeout = 10L;

    private long requestTimeout = 10L;

    private long responseTimeout = 10L;

    private String catalogEndpoint;

    private String catalogSyncCronExpression;
}
