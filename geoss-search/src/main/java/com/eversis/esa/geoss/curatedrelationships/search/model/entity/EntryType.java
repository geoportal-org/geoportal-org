package com.eversis.esa.geoss.curatedrelationships.search.model.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Entry type.
 */
public enum EntryType {

    /**
     * Data entry type.
     */
    DATA("data_resource"),
    /**
     * Service entry type.
     */
    SERVICE("service_resource"),
    /**
     * Information entry type.
     */
    INFORMATION("information_resource");

    @JsonValue
    private String name;

    EntryType(String name) {
        this.name = name;
    }

    /**
     * From string entry type.
     *
     * @param text the text
     * @return the entry type
     */
    @JsonCreator
    public static EntryType fromString(String text) {
        for (EntryType b : EntryType.values()) {
            if (b.getName().equalsIgnoreCase(text)) {
                return b;
            }
        }
        return null;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
