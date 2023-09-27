package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.skip;

import org.springframework.batch.core.step.skip.LimitCheckingItemSkipPolicy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.Collections;

/**
 * The type Data integrity violation skip policy.
 */
@Component
public class DataIntegrityViolationSkipPolicy extends LimitCheckingItemSkipPolicy {

    @Value("${batch.skip-limit}")
    private int skipLimit;

    /**
     * Sets limits.
     */
    @PostConstruct
    public void setupLimits() {
        setSkipLimit(skipLimit);
        setSkippableExceptionMap(Collections.singletonMap(DataIntegrityViolationException.class, true));
    }
}
