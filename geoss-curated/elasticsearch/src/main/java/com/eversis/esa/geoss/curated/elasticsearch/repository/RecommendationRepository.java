package com.eversis.esa.geoss.curated.elasticsearch.repository;

import com.eversis.esa.geoss.curated.elasticsearch.model.RecommendationELK;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Recommendation repository.
 */
@Repository("elasticRecommendationRepository")
public interface RecommendationRepository extends ElasticsearchRepository<RecommendationELK, String> {

    /**
     * Find by recommendation id list.
     *
     * @param recommendationId the recommendation id
     * @return the list
     */
    List<RecommendationELK> findByRecommendationId(Long recommendationId);

    /**
     * Delete by recommendation id list.
     *
     * @param recommendationId the recommendation id
     * @return the list
     */
    List<RecommendationELK> deleteByRecommendationId(Long recommendationId);
}
