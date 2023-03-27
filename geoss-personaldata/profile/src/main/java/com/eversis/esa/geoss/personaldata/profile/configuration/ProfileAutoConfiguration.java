package com.eversis.esa.geoss.personaldata.profile.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Profile autoconfiguration.
 */
@AutoConfiguration
@EnableConfigurationProperties(
        value = {
        }
)
@EntityScan(basePackages = "com.eversis.esa.geoss.personaldata.profile.domain")
@Import(ProfileConfiguration.class)
public class ProfileAutoConfiguration {

}
