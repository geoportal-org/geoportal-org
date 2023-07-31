package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query;

/**
 * The type Ckan query parameters.
 */
class CkanQueryParameters {

    /**
     * The Query.
     */
    static final String QUERY = "q";
    /**
     * The Filter query.
     */
    static final String FILTER_QUERY = "fq";
    /**
     * The Start index.
     */
    static final String START_INDEX = "start";
    /**
     * The Rows count.
     */
    static final String ROWS_COUNT = "rows";
    /**
     * The Facet field.
     */
    static final String FACET_FIELD = "facet.field";
    /**
     * The Value delimiter.
     */
    static final String VALUE_DELIMITER = ":";

    private CkanQueryParameters() {
        throw new IllegalStateException("Utility class");
    }

}
