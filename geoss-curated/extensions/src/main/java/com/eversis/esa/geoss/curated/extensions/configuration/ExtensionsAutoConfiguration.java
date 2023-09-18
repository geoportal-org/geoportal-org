package com.eversis.esa.geoss.curated.extensions.configuration;

import com.eversis.esa.geoss.curated.extensions.properties.ExtensionsProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Extensions auto configuration.
 */
@AutoConfiguration
@ConditionalOnProperty(
        prefix = "geoss.curated.extensions", name = "enabled", havingValue = "true", matchIfMissing = true
)
@EnableConfigurationProperties(
        value = {
                ExtensionsProperties.class
        }
)
@EntityScan(basePackages = "com.eversis.esa.geoss.curated.extensions.domain")
@Import(ExtensionsConfiguration.class)
public class ExtensionsAutoConfiguration {

}
