package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.recommendation;

public class RecommendationElasticsearchFields {

    public static final String TERM_FIELD = "term";
    public static final String TERM_RAW_FIELD = TERM_FIELD + ".raw";
    public static final String TERM_ENGLISH_FIELD = TERM_FIELD + ".english";
    public static final String TERM_TRIGRAM_FIELD = TERM_FIELD + ".trigram";
    public static final String ENTITIES_FIELD = "entities";

    private RecommendationElasticsearchFields() {
        throw new IllegalStateException("Utility class");
    }
}
