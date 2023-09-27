package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * The type Wiki article query factory.
 */
@Component
class WikiArticleQueryFactory {

    @Value("${wikidata.api.url}")
    private String baseUrl;
    @Value("${wikidata.api.pages-chunk-size}")
    private int pagesChunkSize;

    /**
     * Create uri query string.
     *
     * @param pageIds the page ids
     * @return the string
     */
    String createUriQuery(List<String> pageIds) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("action", "query")
                .queryParam("pageids", String.join("|", pageIds))
                .queryParam("prop", "extracts|pageimages|categories|info")
                .queryParam("inprop", "url")
                .queryParam("explaintext") // description
                .queryParam("exintro") // description
                .queryParam("exsentences", 3) // description
                .queryParam("exsectionformat", "plain") // description
                .queryParam("exlimit", pagesChunkSize) // description
                .queryParam("pilimit", pagesChunkSize) // images
                .queryParam("cllimit", "max") // categories
                .queryParam("clshow", "!hidden") // categories
                .queryParam("pithumbsize", "300") // images
                .queryParam("redirects", 1)
                .queryParam("format", "json")
                .build(false).toUriString();
    }

}
