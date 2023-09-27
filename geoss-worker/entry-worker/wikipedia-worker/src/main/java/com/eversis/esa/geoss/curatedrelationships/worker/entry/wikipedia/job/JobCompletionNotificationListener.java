package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * The type Job completion notification listener.
 */
@Log4j2
@Component
class JobCompletionNotificationListener implements JobExecutionListener {

    /**
     * After job.
     *
     * @param jobExecution the job execution
     */
    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            LocalDateTime start = jobExecution.getCreateTime();
            LocalDateTime end = jobExecution.getEndTime();
            long diff = ChronoUnit.MILLIS.between(start, end);
            log.info("JOB FINISHED in {} seconds", TimeUnit.SECONDS.convert(diff, TimeUnit.MILLISECONDS));
        }
    }
}
