package com.eversis.esa.geoss.curatedrelationships.search.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Collection mapper.
 */
public class CollectionMapper {

    private CollectionMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map set set.
     *
     * @param values Comma separated list of string values
     * @return Set of values
     */
    public static Set<String> mapSet(String values) {
        if (values == null) {
            return Collections.emptySet();
        }

        return Arrays.stream(values.split("\\s*,\\s*"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toSet());
    }

    /**
     * Map list list.
     *
     * @param values Comma separated list of string values
     * @return List of values
     */
    public static List<String> mapList(String values) {
        if (values == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(values.split("\\s*,\\s*"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

}
