package com.eversis.esa.geoss.settings.instance.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type System settings autoconfiguration.
 */
@AutoConfiguration
@EnableConfigurationProperties(
        value = {
        }
)
@EntityScan(basePackages = "com.eversis.esa.geoss.settings.instance.domain")
@Import(InstanceSettingsConfiguration.class)
public class InstanceSettingsAutoConfiguration {

}
