package com.eversis.esa.geoss.curatedrelationships.search.repository;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.recommendation.Recommendation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface RecommendationRepository {

    /**
     * Find recommendations for provided term
     *
     * @param term search term
     * @param pageable pagination information
     */
    Page<Recommendation> findRecommendations(@NotBlank String term, @NotNull Pageable pageable);

}
