package com.eversis.esa.geoss.curatedrelationships.search.model.common;

public interface Facet {

    String getTermId();

    String getDisplayName();

    long getCount();

    boolean containsIdentifier();
}
