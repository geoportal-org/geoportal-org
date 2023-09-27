package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job;

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
     * @param ecosystemLoadStep the ecosystem load step
     * @param protectedAreaLoadStep the protected area load step
     * @param storylineLoadStep the storyline load step
     * @param geoDabWorkflowLoadStep the geo dab workflow load step
     * @param vlabDabWorkflowLoadStep the vlab dab workflow load step
     * @param workflowOutputEntriesLoadStep the workflow output entries load step
     * @param cleanUpStep the clean up step
     * @return the job
     */
    @Bean(name = "loadGeodabEntriesJob")
    Job job(
            JobExecutionListener jobExecutionListener,
            @Qualifier("ecosystemEntriesLoadStep") Step ecosystemLoadStep,
            @Qualifier("protectedAreaEntriesLoadStep") Step protectedAreaLoadStep,
            @Qualifier("storylineEntriesLoadStep") Step storylineLoadStep,
            @Qualifier("geoDabWorkflowEntriesLoadStep") Step geoDabWorkflowLoadStep,
            @Qualifier("vlabDabWorkflowEntriesLoadStep") Step vlabDabWorkflowLoadStep,
            @Qualifier("workflowOutputEntriesLoadStep") Step workflowOutputEntriesLoadStep,
            @Qualifier("cleanUpStep") Step cleanUpStep
    ) {
        return new JobBuilder(jobName, jobRepository)
                .start(ecosystemLoadStep)
                .next(protectedAreaLoadStep)
                .next(storylineLoadStep)
                .next(geoDabWorkflowLoadStep)
                .next(vlabDabWorkflowLoadStep)
                .next(workflowOutputEntriesLoadStep)
                .next(cleanUpStep)
                .listener(jobExecutionListener)
                .build();
    }
}
