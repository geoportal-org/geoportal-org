package com.eversis.esa.geoss.curatedrelationships.search.model.common.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.common.Facet;

public class CompoundTermFacet implements Facet {

    private final String termId;
    private final String displayTerm;
    private final long count;

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
