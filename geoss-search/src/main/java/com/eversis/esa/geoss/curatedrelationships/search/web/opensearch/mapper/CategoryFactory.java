package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.mapper;

import com.rometools.rome.feed.atom.Category;

public class CategoryFactory {

    private CategoryFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Category createCategory(String label, String term) {
        Category category = new Category();
        category.setLabel(label);
        category.setTerm(term);
        return category;
    }

}
