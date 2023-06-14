package com.eversis.esa.geoss.curated.recommendations.configuration;

import com.eversis.esa.geoss.curated.recommendations.properties.RecommendationsProperties;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Recommendations auto configuration.
 */
@AutoConfiguration
@ConditionalOnProperty(
        prefix = "geoss.curated.recommendations", name = "enabled", havingValue = "true", matchIfMissing = true
)
@EnableConfigurationProperties(
        value = {
                RecommendationsProperties.class
        }
)
@Import(RecommendationsConfiguration.class)
public class RecommendationsAutoConfiguration {

}
