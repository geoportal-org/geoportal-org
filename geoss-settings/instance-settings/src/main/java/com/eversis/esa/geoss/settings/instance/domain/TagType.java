package com.eversis.esa.geoss.settings.instance.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Tag type.
 */
public enum TagType {

    /**
     * Tip tag type.
     */
    TIP,

    /**
     * New tag type.
     */
    NEW;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * From string tag type.
     *
     * @param category the category
     * @return the tag type
     */
    public static TagType fromString(String category) {
        return TagType.valueOf(category.toUpperCase());
    }
}
