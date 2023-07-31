package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.geosscr;

public class GeossCrEntryElasticsearchFields {

    private GeossCrEntryElasticsearchFields() {
        throw new IllegalStateException("Utility class");
    }

    public static final String SCORE_FIELD = "_score";

    public static final String TITLE_FIELD = "title";
    public static final String TITLE_RAW_FIELD = TITLE_FIELD + ".raw";
    public static final String TITLE_SEARCH_FIELD = TITLE_FIELD + ".title_search";
    public static final String TITLE_TRIGRAM_FIELD = TITLE_FIELD + ".trigram";

    public static final String CODE_FIELD = "code";

    public static final String SUMMARY_FIELD = "summary";
    public static final String SUMMARY_RAW_FIELD = SUMMARY_FIELD + ".raw";
    public static final String SUMMARY_SEARCH_FIELD = SUMMARY_FIELD + ".summary_search";

    public static final String KEYWORDS_FIELD = "keywords";

    public static final String TAGS_FIELD = "tags";
    public static final String TAGS_RAW_FIELD = TAGS_FIELD +  ".raw";
    public static final String TAGS_SEARCH_FIELD = TAGS_FIELD + ".keyword_search";

    public static final String ORGANIZATION_TITLE_RAW_FIELD = "organisation.title.keyword";

    public static final String TRANSFER_OPTIONS_PROTOCOL_FIELD = "transferOptions.protocol";
    public static final String TRANSFER_OPTIONS_NAME_FIELD = "transferOptions.name";
    public static final String TRANSFER_OPTIONS_TITLE_FIELD = "transferOptions.title";

    public static final String PUBLISH_DATE_FIELD = "publishDate";
    public static final String COVERAGE_FIELD = "coverage";
    public static final String RESOURCE_ENTRY_TYPE_FIELD = "type";
    public static final String PARENT_ID_FIELD = "parentId";

    public static final String SOURCE_ID_FIELD = "source.sourceId";
    public static final String SOURCE_TERM_FIELD = "source.term";
    public static final String DATASOURCE_FIELD = "dataSource";
    public static final String DISPLAY_DATASOURCE_FIELD = "displayDataSource";


    public static final String SCORE_WEIGHT_FIELD = "scoreWeight";

    public static final String EXTENSIONS_FIELD = "extensions";
    public static final String EXTENSIONS_SUMMARY_FIELD = EXTENSIONS_FIELD + ".summary";
    public static final String EXTENSIONS_SUMMARY_RAW_FIELD = EXTENSIONS_SUMMARY_FIELD + ".raw";
    public static final String EXTENSIONS_SUMMARY_SEARCH_FIELD = EXTENSIONS_SUMMARY_FIELD + ".summary_search";

}
