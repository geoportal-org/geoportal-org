package com.eversis.esa.geoss.curatedrelationships.search.repository;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.recommendation.Recommendation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * The interface Recommendation repository.
 */
public interface RecommendationRepository {

    /**
     * Find recommendations for provided term.
     *
     * @param term search term
     * @param pageable pagination information
     * @return the page
     */
    Page<Recommendation> findRecommendations(@NotBlank String term, @NotNull Pageable pageable);

}
