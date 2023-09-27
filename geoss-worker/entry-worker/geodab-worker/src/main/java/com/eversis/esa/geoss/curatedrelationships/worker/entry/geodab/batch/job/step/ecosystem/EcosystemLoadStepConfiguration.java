package com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model.Entry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem.listener.EcosystemChunkListener;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.geodab.batch.job.step.ecosystem.listener.EcosystemStepListener;

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
 * The type Ecosystem load step configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration
class EcosystemLoadStepConfiguration {

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
    @Bean("ecosystemChunkListener")
    ChunkListener chunkListener() {
        return new EcosystemChunkListener();
    }

    /**
     * Step listener step execution listener.
     *
     * @return the step execution listener
     */
    @StepScope
    @Bean("ecosystemStepListener")
    StepExecutionListener stepListener() {
        return new EcosystemStepListener();
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
    @Bean("ecosystemEntriesLoadStep")
    Step ecosystemEntriesLoadStep(
            @Qualifier("ecosystemItemReader") ItemReader<Entry> itemReader,
            @Qualifier("ecosystemItemWriter") ItemWriter<Entry> itemWriter,
            @Qualifier("ecosystemChunkListener") ChunkListener chunkListener,
            @Qualifier("ecosystemStepListener") StepExecutionListener stepListener) {
        return new StepBuilder("ecosystemEntriesLoadStep", jobRepository)
                .listener(stepListener)
                .<Entry, Entry>chunk(batchSize, transactionManager)
                .reader(itemReader)
                .writer(itemWriter)
                .listener(chunkListener)
                .build();
    }

}
