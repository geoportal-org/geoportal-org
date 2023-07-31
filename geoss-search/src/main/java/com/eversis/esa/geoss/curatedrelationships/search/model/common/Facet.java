package com.eversis.esa.geoss.curatedrelationships.search.model.common;

/**
 * The interface Facet.
 */
public interface Facet {

    /**
     * Gets term id.
     *
     * @return the term id
     */
    String getTermId();

    /**
     * Gets display name.
     *
     * @return the display name
     */
    String getDisplayName();

    /**
     * Gets count.
     *
     * @return the count
     */
    long getCount();

    /**
     * Contains identifier boolean.
     *
     * @return the boolean
     */
    boolean containsIdentifier();
}
