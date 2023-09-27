package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Collections;
import java.util.Map;

/**
 * The type Articles results.
 */
@Data
public class ArticlesResults {

    @JsonProperty("query")
    private ArticlesWrapper queryResults;

    /**
     * Gets articles.
     *
     * @return the articles
     */
    public Map<String, ArticleDto> getArticles() {
        return queryResults != null ? queryResults.getPages() : Collections.emptyMap();
    }

}
