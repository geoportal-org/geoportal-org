package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The type String builder util tests.
 */
public class StringBuilderUtilTests {

    /**
     * When elements collection is null then throw exception.
     */
    @Test
    void whenElementsCollectionIsNull_thenThrowException() {
        Iterable<String> collection = null;
        String delimiter = ",";
        int maxLength = 10;

        assertThrows(IllegalArgumentException.class,
                () -> StringBuilderUtil.joinElementsUnlessMaxLength(collection, delimiter, maxLength));
    }

    /**
     * When delimiter is null then throw exception.
     */
    @Test
    void whenDelimiterIsNull_thenThrowException() {
        Iterable<String> collection = Collections.emptyList();
        String delimiter = null;
        int maxLength = 10;

        assertThrows(IllegalArgumentException.class,
                () -> StringBuilderUtil.joinElementsUnlessMaxLength(collection, delimiter, maxLength));
    }

    /**
     * When max length not greater than zero then throw exception.
     *
     * @param maxLength the max length
     */
    @ParameterizedTest
    @ValueSource(ints = {-100, 0})
    void whenMaxLengthNotGreaterThanZero_thenThrowException(int maxLength) {
        Iterable<String> collection = Collections.emptyList();
        String delimiter = ",";

        assertThrows(IllegalArgumentException.class,
                () -> StringBuilderUtil.joinElementsUnlessMaxLength(collection, delimiter, maxLength));
    }

    /**
     * When collection total string size is lesser than max length then do not remove elements.
     */
    @Test
    void whenCollectionTotalStringSizeIsLesserThanMaxLength_thenDoNotRemoveElements() {
        Iterable<String> collection = Arrays.asList("abcd", "efgh", "ijklm");
        String delimiter = ",";
        int maxLength = 100;

        String expectedOutput = String.join(delimiter, collection);
        String actualOutput = StringBuilderUtil.joinElementsUnlessMaxLength(collection, delimiter, maxLength);

        assertThat(actualOutput, equalTo(expectedOutput));
    }

    /**
     * When collection total string size is greater than max length then remove elements.
     */
    @Test
    void whenCollectionTotalStringSizeIsGreaterThanMaxLength_thenRemoveElements() {
        Iterable<String> collection = Arrays.asList("abcd", "efgh", "ijkl", "mnop");
        String delimiter = ",";
        int maxLength = 10;

        String expectedOutput = String.join(delimiter, Arrays.asList("abcd", "efgh"));
        String actualOutput = StringBuilderUtil.joinElementsUnlessMaxLength(collection, delimiter, maxLength);

        assertThat(actualOutput, equalTo(expectedOutput));
    }

}
