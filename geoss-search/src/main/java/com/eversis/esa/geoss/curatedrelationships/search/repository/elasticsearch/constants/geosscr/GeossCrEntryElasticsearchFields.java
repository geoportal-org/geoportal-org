package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr;

/**
 * The type Geoss cr entry elasticsearch fields.
 */
public class GeossCrEntryElasticsearchFields {

    private GeossCrEntryElasticsearchFields() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * The constant SCORE_FIELD.
     */
    public static final String SCORE_FIELD = "_score";

    /**
     * The constant TITLE_FIELD.
     */
    public static final String TITLE_FIELD = "title";
    /**
     * The constant TITLE_RAW_FIELD.
     */
    public static final String TITLE_RAW_FIELD = TITLE_FIELD + ".raw";
    /**
     * The constant TITLE_SEARCH_FIELD.
     */
    public static final String TITLE_SEARCH_FIELD = TITLE_FIELD + ".title_search";
    /**
     * The constant TITLE_TRIGRAM_FIELD.
     */
    public static final String TITLE_TRIGRAM_FIELD = TITLE_FIELD + ".trigram";

    /**
     * The constant CODE_FIELD.
     */
    public static final String CODE_FIELD = "code";

    /**
     * The constant SUMMARY_FIELD.
     */
    public static final String SUMMARY_FIELD = "summary";
    /**
     * The constant SUMMARY_RAW_FIELD.
     */
    public static final String SUMMARY_RAW_FIELD = SUMMARY_FIELD + ".raw";
    /**
     * The constant SUMMARY_SEARCH_FIELD.
     */
    public static final String SUMMARY_SEARCH_FIELD = SUMMARY_FIELD + ".summary_search";

    /**
     * The constant KEYWORDS_FIELD.
     */
    public static final String KEYWORDS_FIELD = "keywords";

    /**
     * The constant TAGS_FIELD.
     */
    public static final String TAGS_FIELD = "tags";
    /**
     * The constant TAGS_RAW_FIELD.
     */
    public static final String TAGS_RAW_FIELD = TAGS_FIELD + ".raw";
    /**
     * The constant TAGS_SEARCH_FIELD.
     */
    public static final String TAGS_SEARCH_FIELD = TAGS_FIELD + ".keyword_search";

    /**
     * The constant ORGANIZATION_TITLE_RAW_FIELD.
     */
    public static final String ORGANIZATION_TITLE_RAW_FIELD = "organisation.title.keyword";

    /**
     * The constant TRANSFER_OPTIONS_PROTOCOL_FIELD.
     */
    public static final String TRANSFER_OPTIONS_PROTOCOL_FIELD = "transferOptions.protocol";
    /**
     * The constant TRANSFER_OPTIONS_NAME_FIELD.
     */
    public static final String TRANSFER_OPTIONS_NAME_FIELD = "transferOptions.name";
    /**
     * The constant TRANSFER_OPTIONS_TITLE_FIELD.
     */
    public static final String TRANSFER_OPTIONS_TITLE_FIELD = "transferOptions.title";

    /**
     * The constant PUBLISH_DATE_FIELD.
     */
    public static final String PUBLISH_DATE_FIELD = "publishDate";
    /**
     * The constant COVERAGE_FIELD.
     */
    public static final String COVERAGE_FIELD = "coverage";
    /**
     * The constant RESOURCE_ENTRY_TYPE_FIELD.
     */
    public static final String RESOURCE_ENTRY_TYPE_FIELD = "type";
    /**
     * The constant PARENT_ID_FIELD.
     */
    public static final String PARENT_ID_FIELD = "parentId";

    /**
     * The constant SOURCE_ID_FIELD.
     */
    public static final String SOURCE_ID_FIELD = "source.sourceId";
    /**
     * The constant SOURCE_TERM_FIELD.
     */
    public static final String SOURCE_TERM_FIELD = "source.term";
    /**
     * The constant DATASOURCE_FIELD.
     */
    public static final String DATASOURCE_FIELD = "dataSource";
    /**
     * The constant DISPLAY_DATASOURCE_FIELD.
     */
    public static final String DISPLAY_DATASOURCE_FIELD = "displayDataSource";

    /**
     * The constant SCORE_WEIGHT_FIELD.
     */
    public static final String SCORE_WEIGHT_FIELD = "scoreWeight";

    /**
     * The constant EXTENSIONS_FIELD.
     */
    public static final String EXTENSIONS_FIELD = "extensions";
    /**
     * The constant EXTENSIONS_SUMMARY_FIELD.
     */
    public static final String EXTENSIONS_SUMMARY_FIELD = EXTENSIONS_FIELD + ".summary";
    /**
     * The constant EXTENSIONS_SUMMARY_RAW_FIELD.
     */
    public static final String EXTENSIONS_SUMMARY_RAW_FIELD = EXTENSIONS_SUMMARY_FIELD + ".raw";
    /**
     * The constant EXTENSIONS_SUMMARY_SEARCH_FIELD.
     */
    public static final String EXTENSIONS_SUMMARY_SEARCH_FIELD = EXTENSIONS_SUMMARY_FIELD + ".summary_search";

}
