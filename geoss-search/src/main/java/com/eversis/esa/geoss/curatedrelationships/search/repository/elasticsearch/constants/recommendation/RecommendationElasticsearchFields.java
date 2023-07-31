package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.recommendation;

/**
 * The type Recommendation elasticsearch fields.
 */
public class RecommendationElasticsearchFields {

    /**
     * The constant TERM_FIELD.
     */
    public static final String TERM_FIELD = "term";
    /**
     * The constant TERM_RAW_FIELD.
     */
    public static final String TERM_RAW_FIELD = TERM_FIELD + ".raw";
    /**
     * The constant TERM_ENGLISH_FIELD.
     */
    public static final String TERM_ENGLISH_FIELD = TERM_FIELD + ".english";
    /**
     * The constant TERM_TRIGRAM_FIELD.
     */
    public static final String TERM_TRIGRAM_FIELD = TERM_FIELD + ".trigram";
    /**
     * The constant ENTITIES_FIELD.
     */
    public static final String ENTITIES_FIELD = "entities";

    private RecommendationElasticsearchFields() {
        throw new IllegalStateException("Utility class");
    }
}
