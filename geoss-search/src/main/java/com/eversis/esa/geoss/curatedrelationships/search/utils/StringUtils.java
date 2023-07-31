package com.eversis.esa.geoss.curatedrelationships.search.utils;

public class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static boolean isNotBlank(String s) {
        return s != null && !s.trim().isEmpty();
    }

    public static String wrapWithDoubleQuotes(String initialValue) {
        if (isBlank(initialValue)) {
            return initialValue;
        }

        return "\"" + initialValue + "\"";
    }

}
