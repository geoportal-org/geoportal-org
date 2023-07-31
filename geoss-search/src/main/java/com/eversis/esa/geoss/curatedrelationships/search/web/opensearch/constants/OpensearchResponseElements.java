package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.constants;

/**
 * The type Opensearch response elements.
 */
public class OpensearchResponseElements {

    /**
     * The constant CATEGORY_LABEL_SERIES.
     */
    public static final String CATEGORY_LABEL_SERIES = "series";
    /**
     * The constant CATEGORY_TERM_HLEVEL.
     */
    public static final String CATEGORY_TERM_HLEVEL = "hlevel";
    /**
     * The constant CATEGORY_TERM_KEYWORDS.
     */
    public static final String CATEGORY_TERM_KEYWORDS = "keywords";
    /**
     * The constant CATEGORY_TERM_DATASOURCE.
     */
    public static final String CATEGORY_TERM_DATASOURCE = "dataSource";
    /**
     * The constant CATEGORY_TERM_DISPLAYDATASOURCE.
     */
    public static final String CATEGORY_TERM_DISPLAYDATASOURCE = "displayDataSource";
    /**
     * The constant ACQUISITION.
     */
    public static final String ACQUISITION = "acquisition";

    private OpensearchResponseElements() {
        throw new IllegalStateException("Utility class");
    }

}
