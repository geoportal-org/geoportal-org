package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query;

/**
 * The type Ckan fields.
 */
class CkanFields {

    /**
     * The Id.
     */
    static final String ID = "id";
    /**
     * The Modified date.
     */
    static final String MODIFIED_DATE = "metadata_modified";
    /**
     * The Format.
     */
    static final String FORMAT = "format";
    /**
     * The Tags.
     */
    static final String TAGS = "tags";
    /**
     * The Organization.
     */
    static final String ORGANIZATION = "organization";

    private CkanFields() {
        throw new IllegalStateException("Utility class");
    }

}
