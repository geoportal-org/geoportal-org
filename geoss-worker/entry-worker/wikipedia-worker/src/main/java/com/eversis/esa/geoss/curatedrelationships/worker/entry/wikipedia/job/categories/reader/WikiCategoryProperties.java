package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.reader;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The type Wiki category configuration.
 */
@Component
@ConfigurationProperties(prefix = "wikidata.categories")
@Getter
@Setter
class WikiCategoryProperties {

    private List<String> rootList;
    private List<String> blackList;
    private int depth;

}
