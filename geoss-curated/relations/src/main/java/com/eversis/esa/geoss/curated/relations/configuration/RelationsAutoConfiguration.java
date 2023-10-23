package com.eversis.esa.geoss.curated.relations.configuration;

import com.eversis.esa.geoss.curated.relations.properties.RelationsProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Relations auto configuration.
 */
@AutoConfiguration
@ConditionalOnProperty(
        prefix = "geoss.curated.relations", name = "enabled", havingValue = "true"
)
@EnableConfigurationProperties(
        value = {
                RelationsProperties.class
        }
)
@EntityScan(basePackages = "com.eversis.esa.geoss.curated.relations.domain")
@Import(RelationsConfiguration.class)
public class RelationsAutoConfiguration {

}
