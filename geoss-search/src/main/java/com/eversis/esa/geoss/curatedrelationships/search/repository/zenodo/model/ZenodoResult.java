package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.model;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The type Zenodo result.
 */
@Data
public class ZenodoResult {

    private String id;
    private ZenodoResultMetadata metadata;
    private ZenodoLinks links;
    private ZonedDateTime created;
    private List<ZenodoFile> files = new ArrayList<>();

    /**
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return metadata != null ? metadata.getTitle() : "";
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return metadata != null ? metadata.getDescription() : "";
    }

    /**
     * Get html page link string.
     *
     * @return the string
     */
    public String getHtmlPageLink() {
        return links != null ? links.getLatestHtml() : "";
    }

    /**
     * Gets keywords.
     *
     * @return the keywords
     */
    public List<String> getKeywords() {
        if (metadata != null && metadata.getKeywords() != null) {
            return metadata.getKeywords();
        }
        return Collections.emptyList();
    }

    /**
     * Gets creators.
     *
     * @return the creators
     */
    public List<ZenodoCreator> getCreators() {
        if (metadata != null && metadata.getCreators() != null) {
            return metadata.getCreators();
        }
        return Collections.emptyList();
    }

}
