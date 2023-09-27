package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.query;

import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.constraints.NotNull;

/**
 * The type Vocabulary server query factory.
 */
@Component
@Validated
public class VocabularyServerQueryFactory {

    private static final String TASK_QUERY_PARAM = "task";
    private static final String ARG_QUERY_PARAM = "arg";

    /**
     * Create fetch top terms query string.
     *
     * @param baseUrl the base url
     * @return the string
     */
    public String createFetchTopTermsQuery(@NotNull String baseUrl) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam(TASK_QUERY_PARAM, VocabularyServerTaskType.FETCH_TOP_TERMS.getName())
                .build(true).toUriString();
    }

    /**
     * Create fetch term query string.
     *
     * @param baseUrl the base url
     * @param termId the term id
     * @return the string
     */
    public String createFetchTermQuery(@NotNull String baseUrl, @NotNull String termId) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam(TASK_QUERY_PARAM, VocabularyServerTaskType.FETCH_TERM.getName())
                .queryParam(ARG_QUERY_PARAM, termId)
                .build(true).toUriString();
    }

    /**
     * Create fetch related terms query string.
     *
     * @param baseUrl the base url
     * @param termId the term id
     * @return the string
     */
    public String createFetchRelatedTermsQuery(@NotNull String baseUrl, @NotNull String termId) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam(TASK_QUERY_PARAM, VocabularyServerTaskType.FETCH_RELATED_TERMS.getName())
                .queryParam(ARG_QUERY_PARAM, termId)
                .build(true).toUriString();
    }

    /**
     * Create fetch narrower terms query string.
     *
     * @param baseUrl the base url
     * @param termId the term id
     * @return the string
     */
    public String createFetchNarrowerTermsQuery(@NotNull String baseUrl, @NotNull String termId) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam(TASK_QUERY_PARAM, VocabularyServerTaskType.FETCH_NARROWER_TERMS.getName())
                .queryParam(ARG_QUERY_PARAM, termId)
                .build(true).toUriString();
    }

    /**
     * Create fetch broader terms query string.
     *
     * @param baseUrl the base url
     * @param termId the term id
     * @return the string
     */
    public String createFetchBroaderTermsQuery(@NotNull String baseUrl, @NotNull String termId) {
        return UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam(TASK_QUERY_PARAM, VocabularyServerTaskType.FETCH_BROADER_TERMS.getName())
                .queryParam(ARG_QUERY_PARAM, termId)
                .build(true).toUriString();
    }
}
