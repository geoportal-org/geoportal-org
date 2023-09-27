package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.dto;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Articles wrapper.
 */
@Data
class ArticlesWrapper {

    private Map<String, ArticleDto> pages = new HashMap<>();

}
