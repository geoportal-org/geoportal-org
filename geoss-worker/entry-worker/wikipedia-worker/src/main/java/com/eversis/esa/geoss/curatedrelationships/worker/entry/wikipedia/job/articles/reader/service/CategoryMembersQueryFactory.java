package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * The type Category members query factory.
 */
@Component
class CategoryMembersQueryFactory {

    @Value("${wikidata.api.url}")
    private String baseUrl;

    /**
     * Create uri query string.
     *
     * @param category the category
     * @param continueElement the continue element
     * @return the string
     */
    String createUriQuery(String category, String continueElement) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("action", "query")
                .queryParam("list", "categorymembers")
                .queryParam("cmtype", "page")
                .queryParam("cmtitle", category)
                .queryParam("cmlimit", 50)
                .queryParam("cmcontinue", continueElement)
                .queryParam("format", "json")
                .build(false).toUriString();
    }

}
