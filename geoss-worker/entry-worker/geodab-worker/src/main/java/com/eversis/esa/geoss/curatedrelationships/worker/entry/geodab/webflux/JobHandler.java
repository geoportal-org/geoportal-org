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
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

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
     * Get job.
     *
     * @return the job
     */
    public Mono<JobExecution> getJob() {
        log.debug("getJob:{}", job);
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
     * Run job.
     *
     * @return the mono
     */
    public Mono<JobExecution> runJob() {
        log.debug("runJob:{}", job);
        Mono
                .create(monoSink -> {
                    try {
                        JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
                        monoSink.success(jobExecution);
                    } catch (JobExecutionAlreadyRunningException | JobRestartException
                             | JobInstanceAlreadyCompleteException | JobParametersInvalidException
                             | RuntimeException e) {
                        monoSink.error(e);
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())
                .subscribe(o -> log.info("Job: {}", o), e -> log.error("JobError: " + e.getMessage(), e));
        // delay means waiting for start the previous mono, I don't know a better way to wait for started previous mono
        return Mono.delay(Duration.ofSeconds(1)).flatMap(l -> getJob());
    }
}
