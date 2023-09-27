package com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.listener.SdgEntryChunkListener;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.sdg.batch.job.step.loadsdg.listener.SdgEntryStepListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * The type Sdg entries load step configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration
class SdgEntriesLoadStepConfiguration {

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
    @Bean("unEntryChunkListener")
    ChunkListener chunkListener() {
        return new SdgEntryChunkListener();
    }

    /**
     * Step listener step execution listener.
     *
     * @return the step execution listener
     */
    @StepScope
    @Bean("unEntyStepListener")
    StepExecutionListener stepListener() {
        return new SdgEntryStepListener();
    }

    /**
     * Load un entries step step.
     *
     * @param itemReader the item reader
     * @param itemWriter the item writer
     * @param chunkListener the chunk listener
     * @param stepListener the step listener
     * @return the step
     */
    @Bean("loadUNEntriesStep")
    Step loadUNEntriesStep(
            @Qualifier("sdgEntryItemReader") ItemReader<Entry> itemReader,
            @Qualifier("sdgEntryItemWriter") ItemWriter<Entry> itemWriter,
            @Qualifier("unEntryChunkListener") ChunkListener chunkListener,
            @Qualifier("unEntyStepListener") StepExecutionListener stepListener) {
        return new StepBuilder("loadUNEntriesStep", jobRepository)
                .listener(stepListener)
                .<Entry, Entry>chunk(batchSize, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .listener(chunkListener)
                .build();
    }

}
