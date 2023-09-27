package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.cleanup;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * The type Clean up step configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration
class CleanUpStepConfiguration {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    /**
     * Clean up step step.
     *
     * @param cleanUpTasklet the clean up tasklet
     * @return the step
     */
    @Bean("cleanUpStep")
    Step cleanUpStep(
            CleanUpTasklet cleanUpTasklet) {
        return new StepBuilder("cleanUpStep", jobRepository)
                .tasklet(cleanUpTasklet, transactionManager)
                .build();
    }

}
