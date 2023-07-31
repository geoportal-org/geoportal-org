package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.recommendation.Recommendation;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.ElasticsearchDocumentMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RecommendationResponseMapper extends BaseElasticsearchResponseMapper<Recommendation> {

    @Autowired
    public RecommendationResponseMapper(ElasticsearchDocumentMapper<Recommendation> documentMapper) {
        super(documentMapper);
    }

}
