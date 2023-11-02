package com.eversis.esa.geoss.personaldata.run.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Run autoconfiguration.
 */
@AutoConfiguration
@EnableConfigurationProperties(
        value = {
        }
)
@EntityScan(basePackages = "com.eversis.esa.geoss.personaldata.run.domain")
@Import(RunConfiguration.class)
public class RunAutoConfiguration {

}
