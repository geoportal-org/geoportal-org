package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.query;

/**
 * The enum Vocabulary server task type.
 */
enum VocabularyServerTaskType {

    /**
     * Fetch top terms vocabulary server task type.
     */
    FETCH_TOP_TERMS("fetchTopTerms"),
    /**
     * Fetch term vocabulary server task type.
     */
    FETCH_TERM("fetchTerm"),
    /**
     * Fetch related terms vocabulary server task type.
     */
    FETCH_RELATED_TERMS("fetchRelated"),
    /**
     * Fetch narrower terms vocabulary server task type.
     */
    FETCH_NARROWER_TERMS("fetchDown"),
    /**
     * Fetch broader terms vocabulary server task type.
     */
    FETCH_BROADER_TERMS("fetchUp"),
    ;

    private final String name;

    VocabularyServerTaskType(String name) {
        this.name = name;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    String getName() {
        return name;
    }
}
