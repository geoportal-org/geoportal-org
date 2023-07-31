package com.eversis.esa.geoss.curatedrelationships.search.model.common;

import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;

/**
 * Overriden {@link org.springframework.data.domain.Page} to support pagination using startIndex instead of page number.
 */
public interface Page<T> extends Iterable<T> {

    /**
     * Returns the start index of the current page. Is always non negative.
     *
     * @return the start index of the current page
     */
    int getStartIndex();

    /**
     * Returns the size of the page.
     *
     * @return the size of the page
     */
    int getSize();

    /**
     * Returns the number of elements currently on this page.
     *
     * @return the number of elements currently on this page
     */
    int getNumberOfElements();

    /**
     * Returns the total amount of elements.
     *
     * @return the total amount of elements
     */
    long getTotalElements();

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Iterable#iterator()
     */
    Iterator<T> iterator();

    /**
     * Returns the page content as {@link List}.
     */
    List<T> getContent();

    /**
     * Returns whether the {@link org.springframework.data.domain.Page} has content at all.
     */
    boolean hasContent();

    /**
     * Returns the sorting parameters for the page.
     */
    Sort getSort();

}
