package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.model;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class ZenodoResult {

    private String id;
    private ZenodoResultMetadata metadata;
    private ZenodoLinks links;
    private ZonedDateTime created;
    private List<ZenodoFile> files = new ArrayList<>();

    public String getTitle() {
        return metadata != null ? metadata.getTitle() : "";
    }

    public String getDescription() {
        return metadata != null ? metadata.getDescription() : "";
    }

    public String getHtmlPageLink(){
        return links != null ? links.getLatestHtml() : "";
    }

    public List<String> getKeywords() {
        if (metadata != null && metadata.getKeywords() != null) {
            return metadata.getKeywords();
        }
        return Collections.emptyList();
    }

    public List<ZenodoCreator> getCreators() {
        if (metadata != null && metadata.getCreators() != null) {
            return metadata.getCreators();
        }
        return Collections.emptyList();
    }

}
