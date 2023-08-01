package com.eversis.esa.geoss.curatedrelationships.search.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * The type String utils tests.
 */
public class StringUtilsTests {

    /**
     * When string is null or empty then do not wrap in quotes and return original string.
     *
     * @param input the input
     */
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "   "})
    void whenStringIsNullOrEmpty_thenDoNotWrapInQuotesAndReturnOriginalString(String input) {
        String output = StringUtils.wrapWithDoubleQuotes(input);

        assertThat(output, is(equalTo(input)));
    }

    /**
     * When string contains only alphanumeric chars then wrap in double quotes.
     *
     * @param input the input
     */
    @ParameterizedTest
    @ValueSource(strings = {"text", " text", "text ", " long text ", "\"text\""})
    void whenStringContainsOnlyAlphanumericChars_thenWrapInDoubleQuotes(String input) {
        String output = StringUtils.wrapWithDoubleQuotes(input);

        assertThat(output, not(is(emptyOrNullString())));
        assertThat(output.length(), is(equalTo(input.length() + 2)));
        assertThat(output.charAt(0), is(equalTo('"')));
        assertThat(output.charAt(output.length() - 1), is(equalTo('"')));
    }
}
