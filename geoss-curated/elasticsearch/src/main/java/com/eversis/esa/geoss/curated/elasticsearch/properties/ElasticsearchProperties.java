package com.eversis.esa.geoss.curated.elasticsearch.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * The type Elasticsearch properties.
 */
@Getter
@Setter
@ConfigurationProperties("geoss.curated.elasticsearch")
public class ElasticsearchProperties {

    private boolean enabled;
}
