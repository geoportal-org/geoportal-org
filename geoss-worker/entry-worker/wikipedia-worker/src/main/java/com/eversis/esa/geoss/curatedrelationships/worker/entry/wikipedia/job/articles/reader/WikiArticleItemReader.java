package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model.WikiArticle;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.service.CategoryMembersService;
import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.service.WikiArticleService;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.database.AbstractPagingItemReader;
import org.springframework.batch.item.support.AbstractItemCountingItemStreamItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * The type Wiki article item reader.
 */
@StepScope
@Component("wikiArticleItemReader")
public class WikiArticleItemReader extends AbstractItemCountingItemStreamItemReader<WikiArticle> {

    private final AbstractPagingItemReader<String> categoryReader;
    private final CategoryMembersService categoryMembersService;
    private final WikiArticleService wikiArticleService;

    private ConcurrentLinkedQueue<WikiArticle> wikiArticles = new ConcurrentLinkedQueue<>();

    /**
     * Instantiates a new Wiki article item reader.
     *
     * @param categoryReader the category reader
     * @param categoryMembersService the category members service
     * @param wikiArticleService the wiki article service
     */
    @Autowired
    public WikiArticleItemReader(
            AbstractPagingItemReader<String> categoryReader,
            CategoryMembersService categoryMembersService,
            WikiArticleService wikiArticleService) {
        this.categoryReader = categoryReader;
        this.categoryMembersService = categoryMembersService;
        this.wikiArticleService = wikiArticleService;
        setSaveState(false);
    }

    /**
     * Open.
     *
     * @param executionContext the execution context
     * @throws ItemStreamException the item stream exception
     */
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        super.open(executionContext);
        categoryReader.open(executionContext);
    }

    @Override
    protected WikiArticle doRead() throws Exception {
        WikiArticle wikiArticle = readItem();
        if (wikiArticle != null) {
            return wikiArticle;
        }
        fetchWikiArticles();
        return readItem();
    }

    private WikiArticle readItem() {
        return wikiArticles.poll();
    }

    @Override
    protected void doOpen() throws Exception {
        fetchWikiArticles();
    }

    protected void fetchWikiArticles() throws Exception {
        List<String> currentCategoryPages = new ArrayList<>();
        String currentCategory = categoryReader.read();
        while (currentCategoryPages.isEmpty() && currentCategory != null) {
            currentCategoryPages = categoryMembersService.fetchCategoryMembersPages(currentCategory);

            if (currentCategoryPages.isEmpty()) {
                currentCategory = categoryReader.read();
            }
        }
        wikiArticles.addAll(wikiArticleService.fetchWikiArticles(currentCategoryPages));
    }

    @Override
    protected void doClose() throws Exception {
        // Do nothing
    }

}
