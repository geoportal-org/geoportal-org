package com.eversis.esa.geoss.common.configuration;

import com.eversis.esa.geoss.common.properties.DefaultContactProperties;
import com.eversis.esa.geoss.common.properties.EmailProperties;

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
                EmailProperties.class
        }
)
@EntityScan(basePackages = "com.eversis.esa.geoss.common.domain")
@Import(CommonConfiguration.class)
public class CommonAutoConfiguration {

}
