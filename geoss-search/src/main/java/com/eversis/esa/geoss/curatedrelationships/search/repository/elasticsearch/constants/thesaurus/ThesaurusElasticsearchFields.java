package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.thesaurus;

/**
 * The type Thesaurus elasticsearch fields.
 */
public class ThesaurusElasticsearchFields {

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
     * The constant DEFINITION_FIELD.
     */
    public static final String DEFINITION_FIELD = "definition";
    /**
     * The constant RELATED_CONCEPTS_FIELD.
     */
    public static final String RELATED_CONCEPTS_FIELD = "relatedConcepts";
    /**
     * The constant BROADER_CONCEPTS_FIELD.
     */
    public static final String BROADER_CONCEPTS_FIELD = "broaderConcepts";
    /**
     * The constant NARROWER_CONCEPTS_FIELD.
     */
    public static final String NARROWER_CONCEPTS_FIELD = "narrowerConcepts";
    /**
     * The constant WEIGHT.
     */
    public static final String WEIGHT = "weight";

    private ThesaurusElasticsearchFields() {
        throw new IllegalStateException("Utility class");
    }

}
