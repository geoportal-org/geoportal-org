package com.eversis.esa.geoss.curatedrelationships.search.web.api;

import com.eversis.esa.geoss.curatedrelationships.search.service.recommendation.RecommendationService;
import com.eversis.esa.geoss.curatedrelationships.search.web.api.dto.recommendation.RecommendedResourceDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * The type Recommendation controller.
 */
@Slf4j
@Tag(name = "RECOMMENDATION")
@RequestMapping("/api/recommendations")
@RestController
class RecommendationController {

    private final RecommendationService recommendationService;

    /**
     * Instantiates a new Recommendation controller.
     *
     * @param recommendationService the recommendation service
     */
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    /**
     * Gets recommended resources.
     *
     * @param phrase the phrase
     * @param size the size
     * @return the recommended resources
     */
    @Operation(summary = "Get recommended resources related to search phrase")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<RecommendedResourceDto> getRecommendedResources(
            @Parameter(description = "Search term") @RequestParam(name = "st") @Valid @NotBlank String phrase,
            @Parameter(description = "Max items count") @RequestParam(name = "ct", defaultValue = "10") @Min(
                    1) Integer size) {
        log.debug("Getting recommendations related to phrase: {} with max size: {}", phrase, size);
        return recommendationService.getRecommendations(phrase, size).getContent()
                .stream()
                .map(RecommendedResourceDto::of)
                .collect(Collectors.toList());
    }
}
