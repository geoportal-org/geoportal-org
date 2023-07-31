package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.constants.thesaurus;

public class ThesaurusElasticsearchFields {

    public static final String TERM_FIELD = "term";
    public static final String TERM_RAW_FIELD = TERM_FIELD + ".raw";
    public static final String TERM_ENGLISH_FIELD = TERM_FIELD + ".english";
    public static final String TERM_TRIGRAM_FIELD = TERM_FIELD + ".trigram";
    public static final String DEFINITION_FIELD = "definition";
    public static final String RELATED_CONCEPTS_FIELD = "relatedConcepts";
    public static final String BROADER_CONCEPTS_FIELD = "broaderConcepts";
    public static final String NARROWER_CONCEPTS_FIELD = "narrowerConcepts";
    public static final String WEIGHT = "weight";

    private ThesaurusElasticsearchFields() {
        throw new IllegalStateException("Utility class");
    }

}
