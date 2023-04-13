package com.eversis.esa.geoss.settings.common.configuration;

import com.eversis.esa.geoss.settings.common.properties.DefaultContactProperties;
import com.eversis.esa.geoss.settings.common.properties.EmailProperties;
import com.eversis.esa.geoss.settings.common.properties.GeossProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Common autoconfiguration.
 */
@AutoConfiguration
@EnableConfigurationProperties(
        value = {
                DefaultContactProperties.class,
                EmailProperties.class,
                GeossProperties.class
        }
)
@EntityScan(basePackages = "com.eversis.esa.geoss.settings.common.domain")
@Import(CommonConfiguration.class)
public class CommonAutoConfiguration {

}
