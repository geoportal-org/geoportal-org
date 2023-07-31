package com.eversis.esa.geoss.curatedrelationships.search.model.common.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Facet;

/**
 * The type Compound term facet.
 */
public class CompoundTermFacet implements Facet {

    private final String termId;
    private final String displayTerm;
    private final long count;

    /**
     * Instantiates a new Compound term facet.
     *
     * @param termId the term id
     * @param displayTerm the display term
     * @param count the count
     */
    public CompoundTermFacet(String termId, String displayTerm, long count) {
        this.termId = termId;
        this.displayTerm = displayTerm;
        this.count = count;
    }

    @Override
    public String getTermId() {
        return termId;
    }

    @Override
    public String getDisplayName() {
        return displayTerm;
    }

    @Override
    public long getCount() {
        return count;
    }

    @Override
    public boolean containsIdentifier() {
        return true;
    }
}
