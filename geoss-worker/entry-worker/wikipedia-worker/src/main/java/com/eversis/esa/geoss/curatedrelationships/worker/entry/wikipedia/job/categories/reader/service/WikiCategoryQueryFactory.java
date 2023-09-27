package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.reader.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

/**
 * The type Wiki category query factory.
 */
@Component
class WikiCategoryQueryFactory {

    @Value("${wikidata.categories.sparql.url}")
    private String baseUrl;
    @Value("${wikidata.categories.sparql.default-graph-uri}")
    private String defaultGraphUri;

    /**
     * Create encoded url query uri.
     *
     * @param rootCategory the root category
     * @param depth the depth
     * @return the uri
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @throws URISyntaxException the uri syntax exception
     */
    URI createEncodedUrlQuery(String rootCategory, int depth) throws UnsupportedEncodingException, URISyntaxException {
        String url = baseUrl + "?default-graph-uri=" + defaultGraphUri
                     + "&query=" + URLEncoder.encode(createSparqlQuery(rootCategory, depth),
                StandardCharsets.UTF_8.toString())
                     + "&format=" + URLEncoder.encode("application/sparql-results+json",
                StandardCharsets.UTF_8.toString());
        return new URI(url);
    }

    private String createSparqlQuery(String rootCategory, int depth) {
        String categoryBroaderExpression = String.join("?/", Collections.nCopies(depth, "skos:broader"));
        return "select distinct ?subcategory where "
               + "{?subcategory " + categoryBroaderExpression + " " + rootCategory + "}";
    }

}
