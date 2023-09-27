package com.eversis.esa.geoss.curatedrelationships.thesaurusworker.util;

import lombok.NonNull;

/**
 * The type String utils.
 */
public class StringUtils {

    private StringUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Remove surrounding strings string.
     *
     * @param text the text
     * @param prefix the prefix
     * @param suffix the suffix
     * @return the string
     */
    public static String removeSurroundingStrings(@NonNull String text, @NonNull String prefix,
            @NonNull String suffix) {
        if (text.length() < prefix.length() + suffix.length()) {
            return text;
        }

        if (text.startsWith(prefix) && text.endsWith(suffix)) {
            return text.substring(prefix.length(), text.length() - suffix.length());
        }

        return text;
    }

}
