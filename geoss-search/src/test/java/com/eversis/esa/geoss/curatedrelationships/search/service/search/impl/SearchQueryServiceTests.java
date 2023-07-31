package com.eversis.esa.geoss.curatedrelationships.search.service.search.impl;

import com.eversis.esa.geoss.curatedrelationships.search.model.SearchQueryType;
import com.eversis.esa.geoss.curatedrelationships.search.service.search.SearchQueryService;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

/**
 * The type Search query service tests.
 */
public class SearchQueryServiceTests {

    private SearchQueryService queryService = new SearchQueryServiceImpl();

    /**
     * When search phrase contains multiple words and or delimiter then return full text query type.
     *
     * @param input the input
     */
    @ParameterizedTest
    @ValueSource(strings = {"Water OR sdg", "SDG 1 OR SDG 2"})
    void whenSearchPhraseContainsMultipleWordsAndORDelimiter_thenReturnFullTextQueryType(String input) {
        SearchQueryType queryType = queryService.getSearchQueryType(input);

        assertThat(queryType, is(equalTo(SearchQueryType.FULL_TEXT)));
    }

    /**
     * When search phrase is single word then return full text query type.
     *
     * @param input the input
     */
    @ParameterizedTest
    @ValueSource(strings = {"sdg", "water", "a"})
    void whenSearchPhraseIsSingleWord_thenReturnFullTextQueryType(String input) {
        SearchQueryType queryType = queryService.getSearchQueryType(input);

        assertThat(queryType, is(equalTo(SearchQueryType.FULL_TEXT)));
    }

    /**
     * When search phrase contains multiple words without or delimiter then return keyword query type.
     *
     * @param input the input
     */
    @ParameterizedTest
    @ValueSource(strings = {"sdg cos tam", "water geo", "a b"})
    void whenSearchPhraseContainsMultipleWordsWithoutORDelimiter_thenReturnKeywordQueryType(String input) {
        SearchQueryType queryType = queryService.getSearchQueryType(input);

        assertThat(queryType, is(equalTo(SearchQueryType.KEYWORD)));
    }

}
