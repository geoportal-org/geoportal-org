package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.skippolicy;

import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jakarta.annotation.PostConstruct;
import java.util.Map;

/**
 * The type Output dab skip policy.
 */
@Component
public class OutputDabSkipPolicy extends LimitCheckingItemSkipPolicy {

    @Value("${batch.skip-limit:50}")
    private int skipLimit;

    /**
     * Sets limits.
     */
    @PostConstruct
    public void setupLimits() {
        setSkipLimit(skipLimit);
        setSkippableExceptionMap(Map.of(IllegalStateException.class, true, WebClientResponseException.class, true));
    }
}
