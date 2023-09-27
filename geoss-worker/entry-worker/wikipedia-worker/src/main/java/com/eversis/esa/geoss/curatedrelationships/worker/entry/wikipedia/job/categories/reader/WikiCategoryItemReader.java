package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.reader;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.reader.service.WikiCategoryService;

import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Wiki category item reader.
 */
@Log4j2
@StepScope
@Component("wikiCategoryUrlItemReader")
public class WikiCategoryItemReader implements ItemReader<String> {

    private final WikiCategoryService loaderService;
    private final WikiCategoryProperties categoryConfiguration;
    private List<String> categories;

    /**
     * Instantiates a new Wiki category item reader.
     *
     * @param loaderService the loader service
     * @param categoryConfiguration the category configuration
     */
    public WikiCategoryItemReader(final WikiCategoryService loaderService,
            final WikiCategoryProperties categoryConfiguration) {
        this.loaderService = loaderService;
        this.categoryConfiguration = categoryConfiguration;
    }

    /**
     * Read string.
     *
     * @return the string
     * @throws Exception the exception
     * @throws UnexpectedInputException the unexpected input exception
     * @throws ParseException the parse exception
     * @throws NonTransientResourceException the non transient resource exception
     */
    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (!isCategoryListInitialised()) {
            categories = new LinkedList<>();
            fetchCategories();
        }

        if (!CollectionUtils.isEmpty(categories)) {
            return categories.remove(0);
        }

        return null;
    }

    private boolean isCategoryListInitialised() {
        return categories != null;
    }

    private void fetchCategories() throws UnsupportedEncodingException, URISyntaxException {
        for (String rootCategory : categoryConfiguration.getRootList()) {
            categories.addAll(loaderService.fetchWikipediaCategories(rootCategory, categoryConfiguration.getDepth()));
        }

        categories = categories.stream().distinct().collect(Collectors.toList());

        for (String forbiddenCategory : categoryConfiguration.getBlackList()) {
            categories.removeAll(
                    loaderService.fetchWikipediaCategories(forbiddenCategory, categoryConfiguration.getDepth()));
        }
    }

}
