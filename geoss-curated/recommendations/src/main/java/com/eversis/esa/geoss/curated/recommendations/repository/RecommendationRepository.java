package com.eversis.esa.geoss.curated.recommendations.repository;

import com.eversis.esa.geoss.curated.recommendations.domain.Recommendation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * The interface Recommendation repository.
 */
@RepositoryRestResource(exported = false)
public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {

}
