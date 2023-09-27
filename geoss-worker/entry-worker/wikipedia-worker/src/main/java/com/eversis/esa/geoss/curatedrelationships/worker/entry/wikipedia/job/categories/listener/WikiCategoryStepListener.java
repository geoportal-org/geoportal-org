package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * The type Wiki category step listener.
 */
@Log4j2
public class WikiCategoryStepListener implements StepExecutionListener {

    /**
     * After step exit status.
     *
     * @param stepExecution the step execution
     * @return the exit status
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Loaded {} categories in total", stepExecution.getWriteCount());
        }

        return stepExecution.getExitStatus();
    }
}
