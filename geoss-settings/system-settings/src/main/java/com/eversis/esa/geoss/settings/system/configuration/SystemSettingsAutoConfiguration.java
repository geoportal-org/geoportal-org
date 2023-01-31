package com.eversis.esa.geoss.settings.system.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type System settings auto configuration.
 */
@AutoConfiguration
@EnableConfigurationProperties(
        value = {
        }
)
@EntityScan(basePackages = "com.eversis.esa.geoss.settings.system.domain")
@Import(SystemSettingsConfiguration.class)
public class SystemSettingsAutoConfiguration {

}
