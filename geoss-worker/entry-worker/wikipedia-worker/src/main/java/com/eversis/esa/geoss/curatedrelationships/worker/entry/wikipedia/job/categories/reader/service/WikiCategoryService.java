package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.reader.service;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.reader.dto.CategoriesResultsWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

/**
 * The type Wiki category service.
 */
@Service
public class WikiCategoryService {

    private final WebClient webClient;
    private final WikiCategoryQueryFactory queryFactory;

    /**
     * Instantiates a new Wiki category service.
     *
     * @param webClient the web client
     * @param queryFactory the query factory
     */
    @Autowired
    public WikiCategoryService(@Qualifier("wikidataSparqlClient") WebClient webClient,
            WikiCategoryQueryFactory queryFactory) {
        this.webClient = webClient;
        this.queryFactory = queryFactory;
    }

    /**
     * Returns categories of Wikipedia category tree starting from rootCategory. Because of external API limitation.
     *
     * @param rootCategory the root category
     * @param depth the depth param must be provided and must be greater than 0
     * @return the list
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @throws URISyntaxException the uri syntax exception
     */
    public List<String> fetchWikipediaCategories(String rootCategory, int depth)
            throws UnsupportedEncodingException, URISyntaxException {
        return webClient
                .get()
                .uri(queryFactory.createEncodedUrlQuery(rootCategory, depth))
                .retrieve()
                .bodyToMono(CategoriesResultsWrapper.class)
                .timeout(Duration.ofSeconds(30))
                .map(CategoriesResultsWrapper::getCategories)
                .block();
    }

}
