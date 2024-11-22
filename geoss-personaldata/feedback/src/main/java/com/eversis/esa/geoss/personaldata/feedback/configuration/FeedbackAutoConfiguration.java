package com.eversis.esa.geoss.personaldata.feedback.configuration;

import com.eversis.esa.geoss.personaldata.feedback.properties.FeedbackProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type feedback auto configuration.
 */
@AutoConfiguration
@EnableConfigurationProperties(
        value = {
                FeedbackProperties.class
        }
)
@EntityScan(basePackages = "com.eversis.esa.geoss.personaldata.feedback.domain")
@Import(FeedbackConfiguration.class)
public class FeedbackAutoConfiguration {

}
