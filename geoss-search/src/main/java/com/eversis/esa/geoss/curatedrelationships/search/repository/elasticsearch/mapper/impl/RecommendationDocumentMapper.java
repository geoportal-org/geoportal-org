package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.mapper.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;
import com.eversis.esa.geoss.curatedrelationships.search.model.recommendation.Recommendation;
import com.eversis.esa.geoss.curatedrelationships.search.model.recommendation.RecommendedResource;
import com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model.RecommendedResourceElk;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.recommendation.RecommendationElasticsearchFields.ENTITIES_FIELD;

@Component
public class RecommendationDocumentMapper extends BaseElasticsearchDocumentMapper<Recommendation> {

    @Autowired
    public RecommendationDocumentMapper(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public Recommendation mapToObject(SearchHit searchHit) throws IOException {
        Map source = searchHit != null ? searchHit.getSourceAsMap() : Collections.emptyMap();
        RecommendedResourceElk[] resources = objectMapper.convertValue(source.get(ENTITIES_FIELD), RecommendedResourceElk[].class);
        return mapResources(resources);
    }

    private Recommendation mapResources(RecommendedResourceElk[] resources) {
        if (resources == null) {
            return null;
        }
        List<RecommendedResource> recommendedResources = Arrays.stream(resources)
                .map(resourceElk -> new RecommendedResource(
                        DataSource.fromString(resourceElk.getDataSource()),
                        resourceElk.getCode(),
                        resourceElk.getName(),
                        resourceElk.getDescription(),
                        resourceElk.getOrderWeight())).collect(Collectors.toList());
        return new Recommendation(recommendedResources);
    }
}
