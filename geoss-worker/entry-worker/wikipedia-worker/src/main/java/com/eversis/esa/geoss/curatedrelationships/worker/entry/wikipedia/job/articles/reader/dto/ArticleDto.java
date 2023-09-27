package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.reader.dto;

import com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.articles.model.WikiArticle;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Article dto.
 */
@Data
public class ArticleDto {

    @JsonProperty("pageid")
    private String pageId;
    @JsonProperty("title")
    private String title;
    @JsonProperty("extract")
    private String description;
    @JsonProperty("fullurl")
    private String url;
    @JsonProperty("thumbnail")
    private ImageDto image;
    @JsonProperty("categories")
    private List<CategoryDto> categories = new ArrayList<>();

    /**
     * Gets image url.
     *
     * @return the image url
     */
    public String getImageUrl() {
        return image != null ? image.getUrl() : null;
    }

    /**
     * To wiki article wiki article.
     *
     * @return the wiki article
     */
    public WikiArticle toWikiArticle() {
        return new WikiArticle(
                pageId,
                title,
                description,
                getImageUrl(),
                url,
                categories.stream()
                        .map(CategoryDto::getFormattedTitle)
                        .collect(Collectors.toSet()));
    }

}
