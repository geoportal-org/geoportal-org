package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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

    public String getGeossIndex() {
        return geossIndex;
    }

    public String getGeossType() {
        return geossType;
    }

    public String getThesaurusIndex() {
        return thesaurusIndex;
    }

    public String getThesaurusType() {
        return thesaurusType;
    }

    public String getExtensionsIndex() {
        return extensionsIndex;
    }

    public String getExtensionsType() {
        return extensionsType;
    }

    public String getRecommendationIndex() {
        return recommendationIndex;
    }

    public String getRecommendationType() {
        return recommendationType;
    }
}
