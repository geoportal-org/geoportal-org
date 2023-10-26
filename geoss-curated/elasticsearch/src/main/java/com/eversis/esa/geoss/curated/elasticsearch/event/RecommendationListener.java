package com.eversis.esa.geoss.curated.elasticsearch.event;

import com.eversis.esa.geoss.curated.elasticsearch.service.RecommendationService;
import com.eversis.esa.geoss.curated.recommendations.model.RecommendationModel;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * The type Recommendation listener.
 */
@Log4j2
@RequiredArgsConstructor
@Component("elasticRecommendationListener")
public class RecommendationListener {

    private final RecommendationService recommendationService;

    /**
     * Handle recommendation.
     *
     * @param recommendationModel the recommendation model
     */
    @TransactionalEventListener(RecommendationModel.class)
    public void handleRecommendation(RecommendationModel recommendationModel) {
        log.info("recommendationEvent:{}", recommendationModel);
        try {
            recommendationService.reindex(recommendationModel);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
