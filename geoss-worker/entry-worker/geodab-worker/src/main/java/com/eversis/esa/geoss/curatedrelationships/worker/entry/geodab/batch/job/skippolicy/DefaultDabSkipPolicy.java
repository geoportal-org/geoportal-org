package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.skippolicy;

import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Collections;

/**
 * The type Default dab skip policy.
 */
@Component
public class DefaultDabSkipPolicy extends LimitCheckingItemSkipPolicy {

    @Value("${batch.skip-limit:50}")
    private int skipLimit;

    /**
     * Sets limits.
     */
    @PostConstruct
    public void setupLimits() {
        setSkipLimit(skipLimit);
        setSkippableExceptionMap(Collections.singletonMap(IllegalStateException.class, true));
    }
}
