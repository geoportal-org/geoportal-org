package com.eversis.esa.geoss.curated.extensions.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Extensions properties.
 */
@Getter
@Setter
@ConfigurationProperties("geoss.curated.extensions")
public class ExtensionsProperties {

    private boolean enabled;
}
