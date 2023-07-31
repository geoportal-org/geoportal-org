package com.eversis.esa.geoss.curatedrelationships.search.repository.zenodo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * The type Zenodo links.
 */
@Data
class ZenodoLinks {

    private String self;
    @JsonProperty("latest_html")
    private String latestHtml;

}
