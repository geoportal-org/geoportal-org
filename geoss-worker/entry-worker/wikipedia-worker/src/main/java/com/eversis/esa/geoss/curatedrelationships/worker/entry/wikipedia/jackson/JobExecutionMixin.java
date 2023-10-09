package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.jackson;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.boot.jackson.JsonMixin;

import java.util.Collection;

/**
 * The type Job execution mixin.
 */
@JsonMixin(JobExecution.class)
public abstract class JobExecutionMixin {

    /**
     * Gets step executions.
     *
     * @return the step executions
     */
    @JsonIgnore
    public abstract Collection<StepExecution> getStepExecutions();
}
