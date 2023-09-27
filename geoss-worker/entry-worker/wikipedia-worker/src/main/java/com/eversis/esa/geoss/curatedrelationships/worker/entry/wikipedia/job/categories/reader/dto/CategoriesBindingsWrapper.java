package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.reader.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Categories bindings wrapper.
 */
@Data
@NoArgsConstructor
class CategoriesBindingsWrapper {

    private List<CategoryBindingWrapper> bindings = new ArrayList<>();

}
