package com.eversis.esa.geoss.curatedrelationships.search.utils;

/**
 * The type String utils.
 */
public class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Is blank boolean.
     *
     * @param s the s
     * @return the boolean
     */
    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Is not blank boolean.
     *
     * @param s the s
     * @return the boolean
     */
    public static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /**
     * Wrap with double quotes string.
     *
     * @param initialValue the initial value
     * @return the string
     */
    public static String wrapWithDoubleQuotes(String initialValue) {
        if (isBlank(initialValue)) {
            return initialValue;
        }

        return "\"" + initialValue + "\"";
    }

}
