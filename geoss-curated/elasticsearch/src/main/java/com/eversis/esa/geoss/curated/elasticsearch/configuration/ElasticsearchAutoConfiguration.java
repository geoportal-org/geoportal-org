package com.eversis.esa.geoss.curated.elasticsearch.configuration;

import com.eversis.esa.geoss.curated.elasticsearch.properties.ElasticsearchProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;

/**
 * The type Elasticsearch auto configuration.
 */
@AutoConfiguration
@ConditionalOnProperty(
        prefix = "geoss.curated.elasticsearch", name = "enabled", havingValue = "true"
)
@EnableConfigurationProperties(
        value = {
                ElasticsearchProperties.class
        }
)
@Import(ElasticsearchConfiguration.class)
public class ElasticsearchAutoConfiguration {

}
