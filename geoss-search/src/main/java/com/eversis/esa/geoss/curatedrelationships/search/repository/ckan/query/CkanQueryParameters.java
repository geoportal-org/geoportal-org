package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query;

class CkanQueryParameters {

    static final String QUERY = "q";
    static final String FILTER_QUERY = "fq";
    static final String START_INDEX = "start";
    static final String ROWS_COUNT = "rows";
    static final String FACET_FIELD = "facet.field";
    static final String VALUE_DELIMITER = ":";

    private CkanQueryParameters() {
        throw new IllegalStateException("Utility class");
    }

}
