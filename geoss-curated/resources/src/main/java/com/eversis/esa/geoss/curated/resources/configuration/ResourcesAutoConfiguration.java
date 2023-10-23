package com.eversis.esa.geoss.curated.resources.configuration;

import com.eversis.esa.geoss.curated.resources.properties.ResourcesProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Resources auto configuration.
 */
@AutoConfiguration
@ConditionalOnProperty(
        prefix = "geoss.curated.resources", name = "enabled", havingValue = "true"
)
@EnableConfigurationProperties(
        value = {
                ResourcesProperties.class
        }
)
@EntityScan(basePackages = "com.eversis.esa.geoss.curated.resources.domain")
@Import(ResourcesConfiguration.class)
public class ResourcesAutoConfiguration {

}
