package com.eversis.esa.geoss.curatedrelationships.search.service.recommendation;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.recommendation.RecommendedResource;

import javax.validation.constraints.NotBlank;

public interface RecommendationService {

    Page<RecommendedResource> getRecommendations(@NotBlank String term, int count);

}
