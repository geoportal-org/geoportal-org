package com.eversis.esa.geoss.userdata.common.configuration;

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
@EntityScan(basePackages = "com.eversis.esa.geoss.userdata.common.domain")
@Import(CommonConfiguration.class)
public class CommonAutoConfiguration {

}
