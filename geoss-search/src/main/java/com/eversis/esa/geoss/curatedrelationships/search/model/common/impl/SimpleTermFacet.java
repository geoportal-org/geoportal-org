package com.eversis.esa.geoss.curatedrelationships.search.model.common.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Facet;

/**
 * The type Simple term facet.
 */
public class SimpleTermFacet implements Facet {

    private final String termId;
    private final long count;

    /**
     * Instantiates a new Simple term facet.
     *
     * @param termId the term id
     * @param count the count
     */
    public SimpleTermFacet(String termId, long count) {
        if (termId == null) {
            throw new IllegalArgumentException("Term id cannot be null");
        }
        if (count < 0) {
            throw new IllegalArgumentException("Term count must be grater or equal than zero");
        }
        this.termId = termId;
        this.count = count;
    }

    @Override
    public String getTermId() {
        return termId;
    }

    @Override
    public String getDisplayName() {
        return getTermId();
    }

    @Override
    public long getCount() {
        return count;
    }

    @Override
    public boolean containsIdentifier() {
        return false;
    }

}
