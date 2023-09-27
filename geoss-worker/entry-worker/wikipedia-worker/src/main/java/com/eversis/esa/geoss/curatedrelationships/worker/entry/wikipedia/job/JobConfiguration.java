package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job;

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
     * @param fetchCategoriesStep the fetch categories step
     * @param fetchWikiArticlesStep the fetch wiki articles step
     * @param cleanUpStep the clean up step
     * @param jobExecutionListener the job execution listener
     * @return the job
     */
    @Bean(name = "loadWikiArticlesJob")
    Job job(
            @Qualifier("fetchWikiCategoriesStep") Step fetchCategoriesStep,
            @Qualifier("fetchWikiArticlesStep") Step fetchWikiArticlesStep,
            @Qualifier("cleanUpStep") Step cleanUpStep,
            JobExecutionListener jobExecutionListener) {
        return new JobBuilder(jobName, jobRepository)
                .start(fetchCategoriesStep)
                .next(fetchWikiArticlesStep)
                .next(cleanUpStep)
                .listener(jobExecutionListener)
                .build();
    }
}
