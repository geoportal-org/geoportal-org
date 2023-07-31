package com.eversis.esa.geoss.curatedrelationships.search.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;

public class StringUtilsTests {

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "   "})
    void whenStringIsNullOrEmpty_thenDoNotWrapInQuotesAndReturnOriginalString(String input) {
        String output = StringUtils.wrapWithDoubleQuotes(input);

        assertThat(output, is(equalTo(input)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"text", " text", "text ", " long text ", "\"text\""})
    void whenStringContainsOnlyAlphanumericChars_thenWrapInDoubleQuotes(String input) {
        String output = StringUtils.wrapWithDoubleQuotes(input);

        assertThat(output, not(isEmptyOrNullString()));
        assertThat(output.length(), is(equalTo(input.length() + 2)));
        assertThat(output.charAt(0), is(equalTo('"')));
        assertThat(output.charAt(output.length() - 1), is(equalTo('"')));
    }

}
