package com.eversis.esa.geoss.curated.relations.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Relations properties.
 */
@Getter
@Setter
@ConfigurationProperties("geoss.curated.relations")
public class RelationsProperties {

    private boolean enabled;
}
