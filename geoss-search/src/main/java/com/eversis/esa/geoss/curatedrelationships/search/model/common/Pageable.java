package com.eversis.esa.geoss.curatedrelationships.search.model.common;

import org.springframework.data.domain.Sort;

/**
 * Overriden {@link org.springframework.data.domain.Pageable} to support pagination using startIndex instead of page
 * number.
 */
public interface Pageable {

    /**
     * Returns the start index to be returned.
     *
     * @return the start index to be returned.
     */
    int getStartIndex();

    /**
     * Returns the number of items to be returned.
     *
     * @return the number of items of that page
     */
    int getPageSize();

    /**
     * Returns the sorting parameters.
     *
     * @return the sort
     */
    Sort getSort();
}
