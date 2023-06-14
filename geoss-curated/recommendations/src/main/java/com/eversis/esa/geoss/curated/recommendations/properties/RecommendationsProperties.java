package com.eversis.esa.geoss.curated.recommendations.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Recommendations properties.
 */
@Getter
@Setter
@ConfigurationProperties("geoss.curated.recommendations")
public class RecommendationsProperties {

    private boolean enabled;
}
