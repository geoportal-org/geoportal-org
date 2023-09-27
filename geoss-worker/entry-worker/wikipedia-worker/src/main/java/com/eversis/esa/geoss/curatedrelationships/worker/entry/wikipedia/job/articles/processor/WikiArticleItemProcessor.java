package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.processor;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.constants.WikiArticleConstants;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model.WikiArticle;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model.WikiArticleEntry;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.processor.service.WikiArticleEntryFactory;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * The type Wiki article item processor.
 */
@StepScope
@Component("wikiArticleItemProcessor")
public class WikiArticleItemProcessor implements ItemProcessor<WikiArticle, WikiArticleEntry> {

    private final WikiArticleEntryFactory entryFactory;

    @Value("#{stepExecution.executionContext}")
    private ExecutionContext executionContext;

    /**
     * Instantiates a new Wiki article item processor.
     *
     * @param entryFactory the entry factory
     */
    @Autowired
    public WikiArticleItemProcessor(WikiArticleEntryFactory entryFactory) {
        this.entryFactory = entryFactory;
    }

    /**
     * Process wiki article entry.
     *
     * @param wikiArticle the wiki article
     * @return the wiki article entry
     * @throws Exception the exception
     */
    @Override
    public WikiArticleEntry process(WikiArticle wikiArticle) throws Exception {
        return entryFactory.createWikiArticleEntry(wikiArticle,
                getTypeId(), getDefinitionTypeId(), getAccessPolicyId(), getSourceId(), getDataSourceId(),
                getProtocolId());
    }

    private Integer getTypeId() {
        return executionContext.getInt(WikiArticleConstants.WIKIPEDIA_ARTICLE_GEOSS_TYPE_ID);
    }

    private Integer getDefinitionTypeId() {
        return executionContext.getInt(WikiArticleConstants.WIKIPEDIA_ARTICLE_GEOSS_DEFINITION_TYPE_ID);
    }

    private Integer getAccessPolicyId() {
        return executionContext.getInt(WikiArticleConstants.WIKIPEDIA_ARTICLE_GEOSS_ACCESS_POLICY_ID);
    }

    private Integer getSourceId() {
        return executionContext.getInt(WikiArticleConstants.WIKIPEDIA_GEOSS_SOURCE_ID);
    }

    private Integer getDataSourceId() {
        return executionContext.getInt(WikiArticleConstants.WIKIPEDIA_GEOSS_DATA_SOURCE_ID);
    }

    private Integer getProtocolId() {
        return executionContext.getInt(WikiArticleConstants.WIKIPEDIA_GEOSS_PROTOCOL_ID);
    }
}
