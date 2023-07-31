package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.rometools.rome.feed.atom.Category;

/**
 * The type Category factory.
 */
public class CategoryFactory {

    private CategoryFactory() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Create category category.
     *
     * @param label the label
     * @param term the term
     * @return the category
     */
    public static Category createCategory(String label, String term) {
        Category category = new Category();
        category.setLabel(label);
        category.setTerm(term);
        return category;
    }

}
