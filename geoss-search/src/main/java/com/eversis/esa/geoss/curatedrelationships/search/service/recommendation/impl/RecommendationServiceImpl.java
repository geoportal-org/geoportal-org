package com.eversis.esa.geoss.curatedrelationships.search.service.recommendation.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Page;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.Pageable;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.PageImpl;
import com.eversis.esa.geoss.curatedrelationships.search.model.common.impl.PageRequest;
import com.eversis.esa.geoss.curatedrelationships.search.model.recommendation.Recommendation;
import com.eversis.esa.geoss.curatedrelationships.search.model.recommendation.RecommendedResource;
import com.eversis.esa.geoss.curatedrelationships.search.repository.RecommendationRepository;
import com.eversis.esa.geoss.curatedrelationships.search.service.recommendation.RecommendationService;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.constraints.NotBlank;

/**
 * The type Recommendation service.
 */
@Log4j2
@Service
@Validated
class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;

    /**
     * Instantiates a new Recommendation service.
     *
     * @param recommendationRepository the recommendation repository
     */
    public RecommendationServiceImpl(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    @Override
    public Page<RecommendedResource> getRecommendations(@NotBlank String term, int count) {
        final Pageable pageable = new PageRequest(0, count);
        Page<Recommendation> recommendationResults = recommendationRepository.findRecommendations(term, pageable);

        List<RecommendedResource> recommendedResources = new ArrayList<>();
        if (recommendationResults.hasContent()) {
            recommendedResources = recommendationResults.getContent().get(0).getEntities();
            recommendedResources = recommendedResources.stream()
                    .sorted(Comparator.comparingDouble(RecommendedResource::getOrderWeight))
                    .limit(Math.min(recommendedResources.size(), count)).collect(Collectors.toList());
        }

        return new PageImpl<>(recommendedResources, pageable, recommendationResults.getTotalElements());
    }

}
