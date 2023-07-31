package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.recommendation.Recommendation;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchDocumentMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The type Recommendation response mapper.
 */
@Component
public class RecommendationResponseMapper extends BaseElasticsearchResponseMapper<Recommendation> {

    /**
     * Instantiates a new Recommendation response mapper.
     *
     * @param documentMapper the document mapper
     */
    @Autowired
    public RecommendationResponseMapper(ElasticsearchDocumentMapper<Recommendation> documentMapper) {
        super(documentMapper);
    }

}
