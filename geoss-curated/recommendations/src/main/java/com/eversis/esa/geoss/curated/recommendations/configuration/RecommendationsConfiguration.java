package com.eversis.esa.geoss.curated.recommendations.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * The type Recommendations configuration.
 */
@ComponentScan(
        basePackages = {
        }
)
@PropertySource("classpath:application-recommendations.properties")
@Configuration(proxyBeanMethods = false)
public class RecommendationsConfiguration {

}
