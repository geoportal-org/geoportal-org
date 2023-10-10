package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.listener.WorkflowOutputChunkListener;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflowoutput.listener.WorkflowOutputStepListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.skip.SkipPolicy;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * The type Workflow output step configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration
class WorkflowOutputStepConfiguration {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    @Value("${batch.size:50}")
    private int batchSize;

    /**
     * Chunk listener chunk listener.
     *
     * @return the chunk listener
     */
    @StepScope
    @Bean("workflowOutputChunkListener")
    ChunkListener chunkListener() {
        return new WorkflowOutputChunkListener();
    }

    /**
     * Step listener step execution listener.
     *
     * @return the step execution listener
     */
    @StepScope
    @Bean("workflowOutputStepListener")
    StepExecutionListener stepListener() {
        return new WorkflowOutputStepListener();
    }

    /**
     * Workflow output entries load step step.
     *
     * @param itemReader the item reader
     * @param itemWriter the item writer
     * @param chunkListener the chunk listener
     * @param stepListener the step listener
     * @param skipPolicy the skip policy
     * @return the step
     */
    @Bean("workflowOutputEntriesLoadStep")
    Step workflowOutputEntriesLoadStep(
            @Qualifier("workflowOutputItemReader") ItemReader<Entry> itemReader,
            @Qualifier("workflowOutputItemWriter") ItemWriter<Entry> itemWriter,
            @Qualifier("workflowOutputChunkListener") ChunkListener chunkListener,
            @Qualifier("workflowOutputStepListener") StepExecutionListener stepListener,
            @Qualifier("outputDabSkipPolicy") SkipPolicy skipPolicy
    ) {
        return new StepBuilder("workflowOutputEntriesLoadStep", jobRepository)
                .allowStartIfComplete(true)
                .listener(stepListener)
                .<Entry, Entry>chunk(batchSize, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .faultTolerant().skipPolicy(skipPolicy)
                .listener(chunkListener)
                .build();
    }

}
