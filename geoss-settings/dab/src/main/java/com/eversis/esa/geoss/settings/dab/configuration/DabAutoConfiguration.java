package com.eversis.esa.geoss.settings.dab.configuration;

import com.eversis.esa.geoss.settings.dab.properties.DabProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type  dab auto configuration.
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "geoss.settings.dab", name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties(
        value = {
                DabProperties.class
        }
)
@Import(DabConfiguration.class)
public class DabAutoConfiguration {

}
