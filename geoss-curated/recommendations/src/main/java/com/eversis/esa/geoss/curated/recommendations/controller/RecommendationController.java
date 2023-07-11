package com.eversis.esa.geoss.curated.recommendations.controller;

import com.eversis.esa.geoss.curated.recommendations.model.RecommendationModel;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendedEntityModel;
import com.eversis.esa.geoss.curated.recommendations.service.RecommendationService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.BasePathAwareController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

/**
 * The type Recommendation controller.
 */
@Log4j2
@RequiredArgsConstructor
@BasePathAwareController("/recommendations")
@ResponseBody
@Tag(name = "recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * Find recommendations page.
     *
     * @param page the page
     * @param size the size
     * @return the page
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PORTAL_CONTENT_REVIEWER')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<RecommendationModel> findRecommendations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.debug("Find recommendations page: {}, size: {}", page, size);
        return recommendationService.findAllRecommendations(PageRequest.of(page, size));
    }

    /**
     * Create recommendation.
     *
     * @param recommendationDto the recommendation dto
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PORTAL_CONTENT_REVIEWER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public void createRecommendation(@RequestBody @Valid RecommendationModel recommendationDto) {
        recommendationService.createRecommendation(recommendationDto);
    }

    /**
     * Delete recommendation.
     *
     * @param recommendationId the recommendation id
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PORTAL_CONTENT_REVIEWER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{recommendationId}")
    public void deleteRecommendation(@PathVariable long recommendationId) {
        recommendationService.removeRecommendation(recommendationId);
    }

    /**
     * Update recommended keywords.
     *
     * @param recommendationId the recommendation id
     * @param keywords the keywords
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PORTAL_CONTENT_REVIEWER')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{recommendationId}/keywords")
    public void updateRecommendedKeywords(
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
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PORTAL_CONTENT_REVIEWER')")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{recommendationId}/entities")
    public void addRecommendedEntities(
            @PathVariable long recommendationId,
            @RequestBody @NotEmpty @Valid Set<RecommendedEntityModel> entities) {
        recommendationService.addEntities(recommendationId, entities);
    }

    /**
     * Update recommended entity.
     *
     * @param recommendationId the recommendation id
     * @param entity the entity
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PORTAL_CONTENT_REVIEWER')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{recommendationId}/entities")
    public void updateRecommendedEntity(
            @PathVariable long recommendationId,
            @RequestBody @Valid RecommendedEntityModel entity) {
        recommendationService.updateEntity(recommendationId, entity);
    }

    /**
     * Remove recommended entity.
     *
     * @param recommendationId the recommendation id
     * @param dataSourceCode the data source code
     * @param entityCode the entity code
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PORTAL_CONTENT_REVIEWER')")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/{recommendationId}/entities")
    public void removeRecommendedEntity(
            @PathVariable long recommendationId,
            @RequestParam @NotBlank String dataSourceCode,
            @RequestParam @NotBlank String entityCode) {
        recommendationService.removeEntity(recommendationId, dataSourceCode, entityCode);
    }
}
