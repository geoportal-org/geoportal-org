package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.job.categories.reader.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The type Categories results wrapper.
 */
@Data
@NoArgsConstructor
public class CategoriesResultsWrapper {

    private CategoriesBindingsWrapper results;

    /**
     * Gets categories.
     *
     * @return the categories
     */
    public List<String> getCategories() {
        return results.getBindings().stream()
                .map(CategoryBindingWrapper::getCategoryWrapper)
                .map(CategoryWrapper::getCategoryUrl)
                .collect(Collectors.toList());
    }
}
