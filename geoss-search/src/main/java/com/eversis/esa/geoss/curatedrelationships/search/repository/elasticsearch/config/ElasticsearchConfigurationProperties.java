package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The type Elasticsearch configuration properties.
 */
@Component
public class ElasticsearchConfigurationProperties {

    @Value("${datasource.elasticsearch.geoss.index}")
    private String geossIndex;

    @Value("${datasource.elasticsearch.thesaurus.index}")
    private String thesaurusIndex;

    @Value("${datasource.elasticsearch.extensions.index}")
    private String extensionsIndex;

    @Value("${datasource.elasticsearch.recommendation.index}")
    private String recommendationIndex;

    /**
     * Gets geoss index.
     *
     * @return the geoss index
     */
    public String getGeossIndex() {
        return geossIndex;
    }

    /**
     * Gets thesaurus index.
     *
     * @return the thesaurus index
     */
    public String getThesaurusIndex() {
        return thesaurusIndex;
    }

    /**
     * Gets extensions index.
     *
     * @return the extensions index
     */
    public String getExtensionsIndex() {
        return extensionsIndex;
    }

    /**
     * Gets recommendation index.
     *
     * @return the recommendation index
     */
    public String getRecommendationIndex() {
        return recommendationIndex;
    }
}
