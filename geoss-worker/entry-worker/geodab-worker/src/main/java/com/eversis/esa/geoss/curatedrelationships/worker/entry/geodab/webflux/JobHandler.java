package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.webflux;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobInstance;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * The type Job handler.
 */
@Log4j2
@RequiredArgsConstructor
@Component
public class JobHandler {

    private final JobExplorer jobExplorer;

    private final JobLauncher jobLauncher;

    private final Job job;

    /**
     * Gets job.
     *
     * @return the job
     */
    public Mono<JobExecution> getJob() {
        log.debug("job:{}", job);
        JobInstance lastJobInstance = jobExplorer.getLastJobInstance(job.getName());
        log.debug("lastJobInstance:{}", lastJobInstance);
        if (lastJobInstance != null) {
            JobExecution lastJobExecution = jobExplorer.getLastJobExecution(lastJobInstance);
            log.debug("lastJobExecution:{}", lastJobExecution);
            if (lastJobExecution != null) {
                return Mono.just(lastJobExecution);
            }
        }
        return Mono.empty();
    }

    /**
     * Run job mono.
     *
     * @return the mono
     */
    public Mono<JobExecution> runJob() {
        try {
            log.debug("job:{}", job);
            JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
            log.debug("jobExecution:{}", jobExecution);
            return Mono.just(jobExecution);
        } catch (JobExecutionAlreadyRunningException | JobParametersInvalidException | JobRestartException
                 | JobInstanceAlreadyCompleteException e) {
            return Mono.error(e);
        }
    }
}
