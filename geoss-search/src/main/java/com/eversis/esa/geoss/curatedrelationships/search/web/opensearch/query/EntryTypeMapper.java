package com.eversis.esa.geoss.curatedrelationships.search.web.opensearch.query;

import com.eversis.esa.geoss.curatedrelationships.search.model.entity.EntryType;

import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The type Entry type mapper.
 */
public class EntryTypeMapper {

    private EntryTypeMapper() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Map from string set.
     *
     * @param entryTypes the entry types
     * @return the set
     */
    public static Set<EntryType> mapFromString(String entryTypes) {
        if (entryTypes == null) {
            return Collections.emptySet();
        }

        return Arrays.stream(entryTypes.split("\\s*,\\s*"))
                .map(EntryType::fromString)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
