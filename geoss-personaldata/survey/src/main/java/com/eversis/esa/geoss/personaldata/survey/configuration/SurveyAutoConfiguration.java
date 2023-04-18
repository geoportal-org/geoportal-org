package com.eversis.esa.geoss.personaldata.survey.configuration;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Survey autoconfiguration.
 */
@AutoConfiguration
@EnableConfigurationProperties(
        value = {
        }
)
@EntityScan(basePackages = "com.eversis.esa.geoss.personaldata.survey.domain")
@Import(SurveyConfiguration.class)
public class SurveyAutoConfiguration {

}
