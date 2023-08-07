package com.eversis.esa.geoss.curatedrelationships.search.service.recommendation;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.recommendation.RecommendedResource;

import jakarta.validation.constraints.NotBlank;

/**
 * The interface Recommendation service.
 */
public interface RecommendationService {

    /**
     * Gets recommendations.
     *
     * @param term the term
     * @param count the count
     * @return the recommendations
     */
    Page<RecommendedResource> getRecommendations(@NotBlank String term, int count);

}
