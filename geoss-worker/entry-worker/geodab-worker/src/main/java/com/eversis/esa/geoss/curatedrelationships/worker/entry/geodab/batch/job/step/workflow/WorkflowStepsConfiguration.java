package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.listener.WorkflowChunkListener;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.workflow.listener.WorkflowStepListener;

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
 * The type Workflow steps configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration
class WorkflowStepsConfiguration {

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
    @Bean("workflowChunkListener")
    ChunkListener chunkListener() {
        return new WorkflowChunkListener();
    }

    /**
     * Step listener step execution listener.
     *
     * @return the step execution listener
     */
    @StepScope
    @Bean("workflowStepListener")
    StepExecutionListener stepListener() {
        return new WorkflowStepListener();
    }

    /**
     * Vlab dab workflow entries load step step.
     *
     * @param itemReader the item reader
     * @param itemWriter the item writer
     * @param chunkListener the chunk listener
     * @param stepListener the step listener
     * @param skipPolicy the skip policy
     * @return the step
     */
    @Bean("vlabDabWorkflowEntriesLoadStep")
    Step vlabDabWorkflowEntriesLoadStep(
            @Qualifier("vlabDabWorkflowItemReader") ItemReader<Entry> itemReader,
            @Qualifier("workflowItemWriter") ItemWriter<Entry> itemWriter,
            @Qualifier("workflowChunkListener") ChunkListener chunkListener,
            @Qualifier("workflowStepListener") StepExecutionListener stepListener,
            @Qualifier("defaultDabSkipPolicy") SkipPolicy skipPolicy) {
        return new StepBuilder("vlabDabWorkflowEntriesLoadStep", jobRepository)
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
