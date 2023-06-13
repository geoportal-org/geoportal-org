package com.eversis.esa.geoss.personaldata.common.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Common autoconfiguration.
 */
@AutoConfiguration
@EnableConfigurationProperties(
        value = {
        }
)
@Import(CommonConfiguration.class)
public class CommonAutoConfiguration {

}
