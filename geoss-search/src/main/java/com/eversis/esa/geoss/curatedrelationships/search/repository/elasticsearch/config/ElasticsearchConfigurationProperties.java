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
    @Value("${datasource.elasticsearch.geoss.type}")
    private String geossType;
    @Value("${datasource.elasticsearch.thesaurus.index}")
    private String thesaurusIndex;
    @Value("${datasource.elasticsearch.thesaurus.type}")
    private String thesaurusType;
    @Value("${datasource.elasticsearch.extensions.index}")
    private String extensionsIndex;
    @Value("${datasource.elasticsearch.extensions.type}")
    private String extensionsType;
    @Value("${datasource.elasticsearch.recommendation.index}")
    private String recommendationIndex;
    @Value("${datasource.elasticsearch.recommendation.type}")
    private String recommendationType;

    /**
     * Gets geoss index.
     *
     * @return the geoss index
     */
    public String getGeossIndex() {
        return geossIndex;
    }

    /**
     * Gets geoss type.
     *
     * @return the geoss type
     */
    public String getGeossType() {
        return geossType;
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
     * Gets thesaurus type.
     *
     * @return the thesaurus type
     */
    public String getThesaurusType() {
        return thesaurusType;
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
     * Gets extensions type.
     *
     * @return the extensions type
     */
    public String getExtensionsType() {
        return extensionsType;
    }

    /**
     * Gets recommendation index.
     *
     * @return the recommendation index
     */
    public String getRecommendationIndex() {
        return recommendationIndex;
    }

    /**
     * Gets recommendation type.
     *
     * @return the recommendation type
     */
    public String getRecommendationType() {
        return recommendationType;
    }
}
