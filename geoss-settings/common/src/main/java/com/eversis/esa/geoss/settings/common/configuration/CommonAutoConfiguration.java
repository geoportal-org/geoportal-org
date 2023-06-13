package com.eversis.esa.geoss.settings.common.configuration;

import com.eversis.esa.geoss.settings.common.properties.GeossProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Common autoconfiguration.
 */
@AutoConfiguration
@EnableConfigurationProperties(
        value = {
                GeossProperties.class
        }
)
@Import(CommonConfiguration.class)
public class CommonAutoConfiguration {

}
