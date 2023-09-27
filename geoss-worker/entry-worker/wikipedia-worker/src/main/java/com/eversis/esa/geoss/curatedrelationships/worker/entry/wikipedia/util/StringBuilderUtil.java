package com.eversis.esa.geoss.curatedrelationships.worker.entry.wikipedia.util;

import java.util.StringJoiner;

/**
 * The type String builder util.
 */
public class StringBuilderUtil {

    private StringBuilderUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Joins elements from collection with delimiter unless total string size does not exceeds max length parameter.
     *
     * @param elements the elements
     * @param delimiter the delimiter
     * @param maxLength the max length
     * @return the string
     */
    public static String joinElementsUnlessMaxLength(Iterable<String> elements, String delimiter, int maxLength) {
        if (elements == null) {
            throw new IllegalArgumentException("Elements must not be null");
        }
        if (delimiter == null) {
            throw new IllegalArgumentException("Delimiter must not be null");
        }
        if (maxLength <= 0) {
            throw new IllegalArgumentException("Max length must be greater than 0");
        }
        StringJoiner stringJoiner = new StringJoiner(delimiter);

        for (String element : elements) {
            if (stringJoiner.length() + element.length() > maxLength) {
                break;
            }
            stringJoiner.add(element);
        }

        return stringJoiner.toString();
    }

}
