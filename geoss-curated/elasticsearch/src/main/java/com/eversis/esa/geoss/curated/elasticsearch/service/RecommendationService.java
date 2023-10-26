package com.eversis.esa.geoss.curated.elasticsearch.service;

import com.eversis.esa.geoss.curated.recommendations.model.RecommendationModel;

/**
 * The interface Recommendation service.
 */
public interface RecommendationService {

    /**
     * Index.
     *
     * @param recommendationModel the recommendation model
     */
    void index(RecommendationModel recommendationModel);

    /**
     * Reindex.
     *
     * @param recommendationModel the recommendation model
     */
    void reindex(RecommendationModel recommendationModel);

    /**
     * Delete.
     *
     * @param recommendationId the recommendation id
     */
    void delete(Long recommendationId);
}
