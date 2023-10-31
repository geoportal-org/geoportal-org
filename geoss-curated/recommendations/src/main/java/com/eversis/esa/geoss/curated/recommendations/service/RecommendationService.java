package com.eversis.esa.geoss.curated.recommendations.service;

import com.eversis.esa.geoss.curated.recommendations.model.RecommendationModel;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendedEntityModel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * The interface Recommendation service.
 */
public interface RecommendationService {

    /**
     * Find all recommendations page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<RecommendationModel> findAllRecommendations(@NotNull Pageable pageable);

    /**
     * Gets recommendation.
     *
     * @param recommendationId the recommendation id
     * @return the recommendation
     */
    RecommendationModel getRecommendation(long recommendationId);

    /**
     * Create recommendation.
     *
     * @param recommendationDto the recommendation dto
     * @return the recommendation model
     */
    RecommendationModel createRecommendation(@NotNull RecommendationModel recommendationDto);

    /**
     * Remove recommendation.
     *
     * @param recommendationId the recommendation id
     */
    void removeRecommendation(long recommendationId);

    /**
     * Update keywords.
     *
     * @param recommendationId the recommendation id
     * @param keywords the keywords
     */
    void updateKeywords(long recommendationId, @NotEmpty Set<String> keywords);

    /**
     * Add entities.
     *
     * @param recommendationId the recommendation id
     * @param recommendedEntities the recommended entities
     */
    void addEntities(long recommendationId, @NotEmpty Set<RecommendedEntityModel> recommendedEntities);

    /**
     * Update entity.
     *
     * @param recommendationId the recommendation id
     * @param recommendedEntityDto the recommended entity dto
     */
    void updateEntity(long recommendationId, @NotNull RecommendedEntityModel recommendedEntityDto);

    /**
     * Update entity.
     *
     * @param recommendationId the recommendation id
     * @param recommendedEntityId the recommended entity id
     * @param recommendedEntityDto the recommended entity dto
     */
    void updateEntity(long recommendationId, long recommendedEntityId,
            @NotNull RecommendedEntityModel recommendedEntityDto);

    /**
     * Remove entity.
     *
     * @param recommendationId the recommendation id
     * @param dataSourceCode the data source code
     * @param entityCode the entity code
     */
    void removeEntity(long recommendationId, @NotBlank String dataSourceCode, @NotBlank String entityCode);

    /**
     * Remove entity.
     *
     * @param recommendationId the recommendation id
     * @param recommendedEntityId the recommended entity id
     */
    void removeEntity(long recommendationId, long recommendedEntityId);

    /**
     * Gets data sources codes.
     *
     * @return the data sources codes
     */
    List<String> getDataSourcesCodes();

    /**
     * Publish all page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<RecommendationModel> publishAll(Pageable pageable);
}
