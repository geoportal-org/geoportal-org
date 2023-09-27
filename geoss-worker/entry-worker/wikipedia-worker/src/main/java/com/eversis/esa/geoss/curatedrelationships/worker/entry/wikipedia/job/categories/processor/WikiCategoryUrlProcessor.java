package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.processor;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * The type Wiki category url processor.
 */
@Log4j2
@StepScope
@Component("wikiCategoryUrlItemProcessor")
class WikiCategoryUrlProcessor implements ItemProcessor<String, String> {

    /**
     * Process string.
     *
     * @param categoryUrl the category url
     * @return the string
     * @throws Exception the exception
     */
    @Override
    public String process(String categoryUrl) throws Exception {
        if (categoryUrl.contains("Category:")) {
            return categoryUrl.substring(categoryUrl.indexOf("Category:"));
        }
        return null;
    }
}
