package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.listener.WikiCategoryChunkListener;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.listener.WikiCategoryStepListener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * The type Wiki category fetch step configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration
class WikiCategoryFetchStepConfiguration {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    /**
     * Chunk listener chunk listener.
     *
     * @return the chunk listener
     */
    @StepScope
    @Bean("wikiCategoryChunkListener")
    ChunkListener chunkListener() {
        return new WikiCategoryChunkListener();
    }

    /**
     * Step listener step execution listener.
     *
     * @return the step execution listener
     */
    @StepScope
    @Bean("wikiCategoryStepListener")
    StepExecutionListener stepListener() {
        return new WikiCategoryStepListener();
    }

    /**
     * Fetch wiki categories step step.
     *
     * @param itemReader the item reader
     * @param itemProcessor the item processor
     * @param writer the writer
     * @param chunkListener the chunk listener
     * @param stepListener the step listener
     * @return the step
     */
    @Bean
    Step fetchWikiCategoriesStep(
            @Qualifier("wikiCategoryUrlItemReader") ItemReader<String> itemReader,
            @Qualifier("wikiCategoryUrlItemProcessor") ItemProcessor<String, String> itemProcessor,
            @Qualifier("wikiCategoryItemWriter") ItemWriter<String> writer,
            @Qualifier("wikiCategoryChunkListener") ChunkListener chunkListener,
            @Qualifier("wikiCategoryStepListener") StepExecutionListener stepListener) {
        return new StepBuilder("fetchWikiCategoriesStep", jobRepository)
                .listener(stepListener)
                .<String, String>chunk(100, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(writer)
                .listener(chunkListener)
                .build();
    }

}
