package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.reader.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Category binding wrapper.
 */
@Data
@NoArgsConstructor
class CategoryBindingWrapper {

    @JsonProperty("subcategory")
    private CategoryWrapper categoryWrapper;
}
