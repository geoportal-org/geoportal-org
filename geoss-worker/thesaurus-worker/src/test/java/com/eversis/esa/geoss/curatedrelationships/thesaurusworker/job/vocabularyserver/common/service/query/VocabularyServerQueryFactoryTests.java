package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.job.vocabularyserver.common.service.query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VocabularyServerQueryFactoryTests {

    private VocabularyServerQueryFactory vocabularyServerQueryFactory;

    @BeforeEach
    private void before() {
        vocabularyServerQueryFactory = new VocabularyServerQueryFactory();
    }

    @Test
    void whenCreateFetchTopTermsUriAndBaseUriProvided_thenFullUriIsReturned() {
        String baseUrl = "https://vocabularyserver.com/cnr/ml/eosterm/en/services.php";

        String expectedUri = baseUrl + "?task=fetchTopTerms";
        String fullUri = vocabularyServerQueryFactory.createFetchTopTermsQuery(baseUrl);

        assertThat(expectedUri, equalTo(fullUri));
    }

    @ParameterizedTest
    @MethodSource("createInvalidBaseUri")
    void whenCreateFetchTopTermsUriAndBaseUriNotValid_thenThrowException(String baseUri) {
        assertThrows(IllegalArgumentException.class,
                () -> vocabularyServerQueryFactory.createFetchTopTermsQuery(baseUri));
    }

    @Test
    void whenCreateFetchTermUriAndBaseUriProvided_thenFullUriIsReturned() {
        String baseUrl = "https://vocabularyserver.com/cnr/ml/eosterm/en/services.php";
        String termId = "10";

        String expectedUri = baseUrl + "?task=fetchTerm" + "&arg=" + termId;
        String fullUri = vocabularyServerQueryFactory.createFetchTermQuery(baseUrl, termId);

        assertThat(expectedUri, equalTo(fullUri));
    }

    @ParameterizedTest
    @MethodSource("createInvalidBaseUri")
    void whenCreateFetchTermUriAndBaseUriNotValid_thenThrowException(String baseUri) {
        String termId = "10";
        assertThrows(IllegalArgumentException.class,
                () -> vocabularyServerQueryFactory.createFetchTermQuery(baseUri, termId));
    }

    @Test
    void whenCreateFetchRelatedTermsUriAndBaseUriProvided_thenFullUriIsReturned() {
        String baseUrl = "https://vocabularyserver.com/cnr/ml/eosterm/en/services.php";
        String termId = "10";

        String expectedUri = baseUrl + "?task=fetchRelated" + "&arg=" + termId;
        String fullUri = vocabularyServerQueryFactory.createFetchRelatedTermsQuery(baseUrl, termId);

        assertThat(expectedUri, equalTo(fullUri));
    }

    @ParameterizedTest
    @MethodSource("createInvalidBaseUri")
    void whenCreateFetchRelatedTermsUriAndBaseUriNotValid_thenThrowException(String baseUri) {
        String termId = "10";
        assertThrows(IllegalArgumentException.class,
                () -> vocabularyServerQueryFactory.createFetchRelatedTermsQuery(baseUri, termId));
    }

    @Test
    void whenCreateFetchNarrowerTermsUriAndBaseUriProvided_thenFullUriIsReturned() {
        String baseUrl = "https://vocabularyserver.com/cnr/ml/eosterm/en/services.php";
        String termId = "10";

        String expectedUri = baseUrl + "?task=fetchDown" + "&arg=" + termId;
        String fullUri = vocabularyServerQueryFactory.createFetchNarrowerTermsQuery(baseUrl, termId);

        assertThat(expectedUri, equalTo(fullUri));
    }

    @ParameterizedTest
    @MethodSource("createInvalidBaseUri")
    void whenCreateFetchNarrowerTermsUriAndBaseUriNotValid_thenThrowException(String baseUri) {
        String termId = "10";
        assertThrows(IllegalArgumentException.class,
                () -> vocabularyServerQueryFactory.createFetchNarrowerTermsQuery(baseUri, termId));
    }

    @Test
    void whenCreateFetchBroaderTermsUriAndBaseUriProvided_thenFullUriIsReturned() {
        String baseUrl = "https://vocabularyserver.com/cnr/ml/eosterm/en/services.php";
        String termId = "10";

        String expectedUri = baseUrl + "?task=fetchUp" + "&arg=" + termId;
        String fullUri = vocabularyServerQueryFactory.createFetchBroaderTermsQuery(baseUrl, termId);

        assertThat(expectedUri, equalTo(fullUri));
    }

    @ParameterizedTest
    @MethodSource("createInvalidBaseUri")
    void whenCreateFetchBroaderTermsUriAndBaseUriNotValid_thenThrowException(String baseUri) {
        String termId = "10";
        assertThrows(IllegalArgumentException.class,
                () -> vocabularyServerQueryFactory.createFetchBroaderTermsQuery(baseUri, termId));
    }

    private static Stream<String> createInvalidBaseUri() {
        return Stream.of(
                null,
                "",
                "vocabularyserver.com/cnr/ml/eosterm/en/services",
                "https:/vocabularyserver"
        );
    }

}
