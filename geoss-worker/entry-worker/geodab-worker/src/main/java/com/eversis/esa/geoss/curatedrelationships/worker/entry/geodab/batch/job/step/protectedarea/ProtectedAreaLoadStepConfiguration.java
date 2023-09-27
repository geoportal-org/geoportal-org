package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.listener.ProtectedAreaChunkListener;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.protectedarea.listener.ProtectedAreaStepListener;

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
 * The type Protected area load step configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration
class ProtectedAreaLoadStepConfiguration {

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
    @Bean("protectedAreaChunkListener")
    ChunkListener chunkListener() {
        return new ProtectedAreaChunkListener();
    }

    /**
     * Step listener step execution listener.
     *
     * @return the step execution listener
     */
    @StepScope
    @Bean("protectedAreaStepListener")
    StepExecutionListener stepListener() {
        return new ProtectedAreaStepListener();
    }

    /**
     * Ecosystem entries load step step.
     *
     * @param itemReader the item reader
     * @param itemWriter the item writer
     * @param chunkListener the chunk listener
     * @param stepListener the step listener
     * @return the step
     */
    @Bean("protectedAreaEntriesLoadStep")
    Step ecosystemEntriesLoadStep(
            @Qualifier("protectedAreaItemReader") ItemReader<Entry> itemReader,
            @Qualifier("protectedAreaItemWriter") ItemWriter<Entry> itemWriter,
            @Qualifier("protectedAreaChunkListener") ChunkListener chunkListener,
            @Qualifier("protectedAreaStepListener") StepExecutionListener stepListener) {
        return new StepBuilder("protectedAreaEntriesLoadStep", jobRepository)
                .listener(stepListener)
                .<Entry, Entry>chunk(batchSize, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .listener(chunkListener)
                .build();
    }

}
