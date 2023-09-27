package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.listener;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

/**
 * The type Sdg entry step listener.
 */
@Log4j2
@Component
public class SdgEntryStepListener implements StepExecutionListener {

    /**
     * After step exit status.
     *
     * @param stepExecution the step execution
     * @return the exit status
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Loaded {} UN entries in total", stepExecution.getWriteCount());
        }

        return stepExecution.getExitStatus();
    }
}
