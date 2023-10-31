package com.eversis.esa.geoss.curated.recommendations.controller;

import com.eversis.esa.geoss.common.hateoas.PageMapper;
import com.eversis.esa.geoss.curated.recommendations.domain.Recommendation;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendationModel;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendedEntityModel;
import com.eversis.esa.geoss.curated.recommendations.service.RecommendationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.data.web.PageableDefault;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.EntityLinks;
import org.springframework.hateoas.server.ExposesResourceFor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * The type Recommendation controller.
 */
@Log4j2
@RequiredArgsConstructor
@ExposesResourceFor(Recommendation.class)
@BasePathAwareController("/recommendations")
@ResponseBody
@Tag(name = "recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    private final PageMapper pageMapper;

    private final EntityLinks entityLinks;

    /**
     * Data sources list.
     *
     * @return the list
     */
    @PreAuthorize("permitAll()")
    @RequestMapping(path = "/data-sources-codes", method = RequestMethod.OPTIONS)
    List<String> dataSources() {
        return recommendationService.getDataSourcesCodes();
    }

    /**
     * Find recommendations page.
     *
     * @param pageable the pageable
     * @return the page
     */
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    PagedModel<EntityModel<RecommendationModel>> findRecommendations(
            @ParameterObject @PageableDefault Pageable pageable) {
        log.debug("Find recommendations pageable: {}", pageable);
        Page<RecommendationModel> allRecommendations = recommendationService.findAllRecommendations(pageable);
        return pageMapper.toPagedModel(allRecommendations, RecommendationModel.class, this::recommendationLinks,
                this::recommendationLinks);
    }

    /**
     * Get recommendation.
     *
     * @param recommendationId the recommendation id
     * @return the entity model
     */
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = {"/{recommendationId}"})
    EntityModel<RecommendationModel> getRecommendation(@PathVariable Long recommendationId) {
        RecommendationModel recommendationModel = recommendationService.getRecommendation(recommendationId);
        return EntityModel.of(recommendationModel, recommendationLinks(recommendationModel));
    }

    /**
     * Create recommendation.
     *
     * @param recommendationModel the recommendation model
     * @return the entity model
     */
    @PreAuthorize("hasAnyRole('RECOMMENDATION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    EntityModel<RecommendationModel> createRecommendation(@RequestBody @Valid RecommendationModel recommendationModel) {
        RecommendationModel recommendation = recommendationService.createRecommendation(recommendationModel);
        return EntityModel.of(recommendation, recommendationLinks(recommendation));
    }

    /**
     * Delete recommendation.
     *
     * @param recommendationId the recommendation id
     */
    @PreAuthorize("hasAnyRole('RECOMMENDATION_REMOVER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{recommendationId}")
    void deleteRecommendation(@PathVariable long recommendationId) {
        recommendationService.removeRecommendation(recommendationId);
    }

    /**
     * Update recommended keywords.
     *
     * @param recommendationId the recommendation id
     * @param keywords the keywords
     */
    @PreAuthorize("hasAnyRole('RECOMMENDATION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{recommendationId}/keywords")
    void updateRecommendedKeywords(
            @PathVariable long recommendationId,
            @RequestBody @NotEmpty Set<String> keywords) {
        recommendationService.updateKeywords(recommendationId, keywords);
    }

    /**
     * Add recommended entities.
     *
     * @param recommendationId the recommendation id
     * @param entities the entities
     */
    @PreAuthorize("hasAnyRole('RECOMMENDATION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{recommendationId}/entities")
    void addRecommendedEntities(
            @PathVariable long recommendationId,
            @RequestBody @NotEmpty @Valid Set<RecommendedEntityModel> entities) {
        recommendationService.addEntities(recommendationId, entities);
    }

    /**
     * Update recommended entity.
     *
     * @param recommendationId the recommendation id
     * @param entityId the entity id
     * @param recommendedEntityModel the recommended entity model
     */
    @PreAuthorize("hasAnyRole('RECOMMENDATION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{recommendationId}/entities/{entityId}")
    void updateRecommendedEntity(
            @PathVariable long recommendationId, @PathVariable long entityId,
            @RequestBody @Valid RecommendedEntityModel recommendedEntityModel) {
        recommendationService.updateEntity(recommendationId, entityId, recommendedEntityModel);
    }

    /**
     * Remove recommended entity.
     *
     * @param recommendationId the recommendation id
     * @param entityId the entity id
     */
    @PreAuthorize("hasAnyRole('RECOMMENDATION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{recommendationId}/entities/{entityId}")
    void removeRecommendedEntity(
            @PathVariable long recommendationId, @PathVariable long entityId) {
        recommendationService.removeEntity(recommendationId, entityId);
    }

    /**
     * Reindex recommendations.
     *
     * @param pageable the pageable
     * @return the paged model
     */
    @PreAuthorize("hasAnyRole('RECOMMENDATION_WRITER', 'ADMIN')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @PatchMapping
    PagedModel<EntityModel<RecommendationModel>> publishRecommendations(
            @ParameterObject @PageableDefault Pageable pageable) {
        log.debug("Publish recommendations pageable: {}", pageable);
        Page<RecommendationModel> allRecommendations = recommendationService.publishAll(pageable);
        return pageMapper.toPagedModel(allRecommendations, RecommendationModel.class, this::recommendationLinks,
                this::recommendationLinks);
    }

    private List<Link> recommendationLinks(RecommendationModel recommendationModel) {
        if (recommendationModel == null) {
            return Collections.emptyList();
        }
        return List.of(
                entityLinks.linkToItemResource(Recommendation.class, recommendationModel.getId())
        );
    }

    private List<Link> recommendationLinks() {
        return List.of(
                entityLinks.linkToCollectionResource(Recommendation.class)
        );
    }
}
