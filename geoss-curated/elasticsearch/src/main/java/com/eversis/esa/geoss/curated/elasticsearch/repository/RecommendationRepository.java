package com.eversis.esa.geoss.curated.elasticsearch.repository;

import com.eversis.esa.geoss.curated.elasticsearch.model.RecommendationELK;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * The interface Recommended repository.
 */
@Repository("elasticRecommendationRepository")
public interface RecommendationRepository extends ElasticsearchRepository<RecommendationELK, String> {

}
