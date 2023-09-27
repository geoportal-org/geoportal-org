package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.util;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StringUtilsTests {

    @Test
    void whenTextNotPresent_thenThrowException() {
        String text = null;
        String prefix = "[";
        String suffix = "]";

        assertThrows(NullPointerException.class,
                () -> StringUtils.removeSurroundingStrings(text, prefix, suffix));
    }

    @Test
    void whenPrefixNotPresent_thenThrowException() {
        String text = "[MENTAL CONSTRUCTS]";
        String prefix = null;
        String suffix = "]";

        assertThrows(NullPointerException.class,
                () -> StringUtils.removeSurroundingStrings(text, prefix, suffix));
    }

    @Test
    void whenSuffixNotPresent_thenThrowException() {
        String text = "[MENTAL CONSTRUCTS]";
        String prefix = "[";
        String suffix = null;

        assertThrows(NullPointerException.class,
                () -> StringUtils.removeSurroundingStrings(text, prefix, suffix));
    }

    @Test
    void whenPrefixAndSuffixArePresent_thenTextIsTrimmed() {
        String text = "[MENTAL CONSTRUCTS]";
        String prefix = "[";
        String suffix = "]";

        String expectedOutput = "MENTAL CONSTRUCTS";
        String output = StringUtils.removeSurroundingStrings(text, prefix, suffix);

        assertThat(output, is(equalTo(expectedOutput)));
    }

}
