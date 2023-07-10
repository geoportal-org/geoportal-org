package com.eversis.esa.geoss.curated.recommendations.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * The type Recommendations configuration.
 */
@EnableJpaRepositories(basePackages = "com.eversis.esa.geoss.curated.recommendations.repository")
@ComponentScan(
        basePackages = {
                "com.eversis.esa.geoss.curated.recommendations.service.internal",
                "com.eversis.esa.geoss.curated.recommendations.support.internal"
        }
)
@PropertySource("classpath:application-recommendations.properties")
@Configuration(proxyBeanMethods = false)
public class RecommendationsConfiguration {

}
