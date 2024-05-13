package com.eversis.esa.geoss.settings.site.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Site auto configuration.
 */
@AutoConfiguration
@EnableConfigurationProperties(
        value = {
        }
)
@Import(SiteConfiguration.class)
public class SiteAutoConfiguration {

}
