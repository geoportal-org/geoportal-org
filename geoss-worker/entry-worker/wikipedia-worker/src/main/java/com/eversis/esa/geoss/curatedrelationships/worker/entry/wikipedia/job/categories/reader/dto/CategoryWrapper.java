package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.reader.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Category wrapper.
 */
@Data
@NoArgsConstructor
class CategoryWrapper {

    @JsonProperty("value")
    private String categoryUrl;

}
