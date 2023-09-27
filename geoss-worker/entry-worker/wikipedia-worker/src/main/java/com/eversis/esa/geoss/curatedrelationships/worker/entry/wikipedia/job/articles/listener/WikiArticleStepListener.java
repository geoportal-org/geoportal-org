package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.listener;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.constants.WikiArticleConstants;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.service.database.WikipediaDatabaseService;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * The type Wiki article step listener.
 */
@Log4j2
public class WikiArticleStepListener implements StepExecutionListener {

    private final WikipediaDatabaseService wikipediaDatabaseService;

    /**
     * Instantiates a new Wiki article step listener.
     *
     * @param wikipediaDatabaseService the wikipedia database service
     */
    public WikiArticleStepListener(WikipediaDatabaseService wikipediaDatabaseService) {
        this.wikipediaDatabaseService = wikipediaDatabaseService;
    }

    /**
     * Before step.
     *
     * @param stepExecution the step execution
     */
    @Override
    public void beforeStep(StepExecution stepExecution) {
        Integer wikiArticleTypeId = wikipediaDatabaseService.getArticleTypeId();
        stepExecution.getExecutionContext()
                .putInt(WikiArticleConstants.WIKIPEDIA_ARTICLE_GEOSS_TYPE_ID, wikiArticleTypeId);
        Integer wikiArticleDefinitionTypeId = wikipediaDatabaseService.getArticleDefinitionTypeId();
        stepExecution.getExecutionContext()
                .putInt(WikiArticleConstants.WIKIPEDIA_ARTICLE_GEOSS_DEFINITION_TYPE_ID, wikiArticleDefinitionTypeId);
        Integer wikiArticleAccessPolicyId = wikipediaDatabaseService.getArticleAccessPolicyId();
        stepExecution.getExecutionContext()
                .putInt(WikiArticleConstants.WIKIPEDIA_ARTICLE_GEOSS_ACCESS_POLICY_ID, wikiArticleAccessPolicyId);
        Integer wikipediaSourceId = wikipediaDatabaseService.getWikipediaSourceId();
        stepExecution.getExecutionContext().putInt(WikiArticleConstants.WIKIPEDIA_GEOSS_SOURCE_ID, wikipediaSourceId);
        Integer wikipediaDataSourceId = wikipediaDatabaseService.getWikipediaDataSourceId();
        stepExecution.getExecutionContext()
                .putInt(WikiArticleConstants.WIKIPEDIA_GEOSS_DATA_SOURCE_ID, wikipediaDataSourceId);
        Integer wikipediaProtocolId = wikipediaDatabaseService.getWikipediaProtocolId();
        stepExecution.getExecutionContext()
                .putInt(WikiArticleConstants.WIKIPEDIA_GEOSS_PROTOCOL_ID, wikipediaProtocolId);
    }

    /**
     * After step exit status.
     *
     * @param stepExecution the step execution
     * @return the exit status
     */
    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        if (stepExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Loaded {} articles in total", stepExecution.getWriteCount());
        }

        return stepExecution.getExitStatus();
    }
}
