package com.eversis.esa.geoss.curated.elasticsearch.service.impl;

import com.eversis.esa.geoss.curated.elasticsearch.model.RecommendationELK;
import com.eversis.esa.geoss.curated.elasticsearch.model.RecommendedResourceELK;
import com.eversis.esa.geoss.curated.elasticsearch.repository.RecommendationRepository;
import com.eversis.esa.geoss.curated.elasticsearch.service.RecommendationService;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendationModel;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendedEntityModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Recommendation service.
 */
@Log4j2
@RequiredArgsConstructor
@Service("elasticRecommendationService")
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;

    @Override
    public void index(RecommendationModel recommendationModel) {
        log.debug("recommendationModel:{}", recommendationModel);
        Set<String> keywords = recommendationModel.getKeywords();
        for (String keyword : keywords) {
            RecommendationELK recommendationELK = new RecommendationELK();
            recommendationELK.setTerm(keyword);
            recommendationELK.setRecommendationId(recommendationModel.getId());
            Set<RecommendedResourceELK> entities = map(recommendationModel.getEntities());
            recommendationELK.setEntities(entities);
            log.debug("recommendation:{}", recommendationELK);

            RecommendationELK recommendationELKresult = recommendationRepository.save(recommendationELK);
            log.debug("recommendation:{}", recommendationELKresult);
        }
    }

    @Override
    public void reindex(RecommendationModel recommendationModel) {
        log.debug("recommendationModel:{}", recommendationModel);

        Set<String> terms = recommendationRepository.findByRecommendationId(recommendationModel.getId()).stream()
                .map(RecommendationELK::getTerm)
                .collect(Collectors.toSet());
        log.debug("terms:{}", terms);

        Set<String> keywords = recommendationModel.getKeywords();
        log.debug("keywords:{}", keywords);
        for (String keyword : keywords) {
            RecommendationELK recommendation = recommendationRepository.findById(keyword).map(recommendationELK1 -> {
                recommendationELK1.increment();
                return recommendationELK1;
            }).orElseGet(() -> {
                RecommendationELK recommendationELK1 = new RecommendationELK();
                recommendationELK1.setTerm(keyword);
                recommendationELK1.setRecommendationId(recommendationModel.getId());
                return recommendationELK1;
            });

            Set<RecommendedResourceELK> entities = map(recommendationModel.getEntities());
            recommendation.setEntities(entities);
            log.debug("recommendation:{}", recommendation);

            RecommendationELK recommendationELKresult = recommendationRepository.save(recommendation);
            log.debug("recommendation:{}", recommendationELKresult);

            terms.remove(keyword);
        }

        log.debug("terms:{}", terms);
        recommendationRepository.deleteAllById(terms);
    }

    @Override
    public void delete(Long recommendationId) {
        log.debug("recommendationId:{}", recommendationId);
        List<RecommendationELK> recommendations = recommendationRepository.deleteByRecommendationId(recommendationId);
        log.debug("recommendations:{}", recommendations);
    }

    private Set<RecommendedResourceELK> map(Set<RecommendedEntityModel> recommendedEntities) {
        Set<RecommendedResourceELK> recommendedResources = new HashSet<>();
        for (RecommendedEntityModel recommendedEntity : recommendedEntities) {
            RecommendedResourceELK recommendedResource = new RecommendedResourceELK();
            recommendedResource.setName(recommendedEntity.getName());
            recommendedResource.setCode(recommendedEntity.getEntityCode());
            recommendedResource.setDataSource(recommendedEntity.getDataSourceCode());
            recommendedResource.setOrderWeight(recommendedEntity.getOrderWeight());
            recommendedResources.add(recommendedResource);
        }
        return recommendedResources;
    }
}
