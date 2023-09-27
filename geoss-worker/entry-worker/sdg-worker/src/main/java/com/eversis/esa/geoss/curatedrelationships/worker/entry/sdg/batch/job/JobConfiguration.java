package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

/**
 * The type Job configuration.
 */
@RequiredArgsConstructor
@Configuration
class JobConfiguration {

    private final JobRepository jobRepository;

    @Value("${spring.batch.job.name}")
    private String jobName;

    /**
     * Job job.
     *
     * @param jobExecutionListener the job execution listener
     * @param step the step
     * @param cleanUpStep the clean up step
     * @return the job
     */
    @Bean(name = "loadSdgEntriesJob")
    Job job(
            JobExecutionListener jobExecutionListener,
            @Qualifier("loadUNEntriesStep") Step step,
            @Qualifier("cleanUpStep") Step cleanUpStep) {
        return new JobBuilder(jobName, jobRepository)
                .start(step)
                .next(cleanUpStep)
                .listener(jobExecutionListener)
                .build();
    }
}
