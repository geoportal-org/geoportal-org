package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.listener.WikiArticleChunkListener;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.listener.WikiArticleStepListener;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model.WikiArticle;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model.WikiArticleEntry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.service.database.WikipediaDatabaseService;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.skip.DataIntegrityViolationSkipPolicy;

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
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

/**
 * The type Wiki article fetch step configuration.
 */
@Log4j2
@RequiredArgsConstructor
@Configuration
class WikiArticleFetchStepConfiguration {

    private final JobRepository jobRepository;

    private final PlatformTransactionManager transactionManager;

    /**
     * Chunk listener chunk listener.
     *
     * @return the chunk listener
     */
    @StepScope
    @Bean("wikiArticleChunkListener")
    ChunkListener chunkListener() {
        return new WikiArticleChunkListener();
    }

    /**
     * Step listener step execution listener.
     *
     * @param wikipediaDatabaseService the wikipedia database service
     * @return the step execution listener
     */
    @StepScope
    @Bean("wikiArticleStepListener")
    StepExecutionListener stepListener(WikipediaDatabaseService wikipediaDatabaseService) {
        return new WikiArticleStepListener(wikipediaDatabaseService);
    }

    /**
     * Fetch wiki articles step step.
     *
     * @param itemReader the item reader
     * @param itemProcessor the item processor
     * @param itemWriter the item writer
     * @param chunkListener the chunk listener
     * @param stepListener the step listener
     * @param taskExecutor the task executor
     * @param skipPolicy the skip policy
     * @return the step
     */
    @Bean
    Step fetchWikiArticlesStep(
            @Qualifier("wikiArticleItemReader") ItemReader<WikiArticle> itemReader,
            @Qualifier("wikiArticleItemProcessor") ItemProcessor<WikiArticle, WikiArticleEntry> itemProcessor,
            @Qualifier("wikiArticleItemWriter") ItemWriter<WikiArticleEntry> itemWriter,
            @Qualifier("wikiArticleChunkListener") ChunkListener chunkListener,
            @Qualifier("wikiArticleStepListener") StepExecutionListener stepListener,
            @Qualifier("batchTaskExecutor") TaskExecutor taskExecutor,
            DataIntegrityViolationSkipPolicy skipPolicy) {
        return new StepBuilder("fetchWikiArticlesStep", jobRepository)
                .listener(stepListener)
                .<WikiArticle, WikiArticleEntry>chunk(50, transactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .faultTolerant().skipPolicy(skipPolicy)
                .listener(chunkListener)
                .taskExecutor(taskExecutor)
                .build();
    }

}
