package com.eversis.esa.geoss.curatedrelationships.search.repository.ckan.query;

class CkanFields {

    static final String ID = "id";
    static final String MODIFIED_DATE = "metadata_modified";
    static final String FORMAT = "format";
    static final String TAGS = "tags";
    static final String ORGANIZATION = "organization";
    private CkanFields() {
        throw new IllegalStateException("Utility class");
    }

}
