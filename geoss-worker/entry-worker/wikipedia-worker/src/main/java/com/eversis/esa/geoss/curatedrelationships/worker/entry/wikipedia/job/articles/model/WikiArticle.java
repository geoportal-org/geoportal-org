package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * The type Wiki article.
 */
@Data
@EqualsAndHashCode(of = "pageId")
public class WikiArticle {

    private final String pageId;
    private final String title;
    private final String description;
    private final String imageUrl;
    private final String url;
    private final Set<String> categories;

}
