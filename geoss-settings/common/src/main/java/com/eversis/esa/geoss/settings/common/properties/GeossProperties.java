package com.eversis.esa.geoss.settings.common.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Geoss properties.
 */
@Getter
@Setter
@ConfigurationProperties("com.eversis.esa.geoss.settings")
public class GeossProperties {

    private String[] supportedLanguages;

}
