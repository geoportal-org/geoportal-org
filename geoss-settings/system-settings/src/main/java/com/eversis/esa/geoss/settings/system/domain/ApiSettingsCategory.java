package com.eversis.esa.geoss.settings.system.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Api settings category.
 */
public enum ApiSettingsCategory {

    /**
     * External api settings category.
     */
    EXTERNAL,

    /**
     * Dab api settings category.
     */
    DAB,

    /**
     * Knowledge producer api settings category.
     */
    KNOWLEDGE_PRODUCER,

    /**
     * Curated api settings category.
     */
    CURATED,

    /**
     * Other api settings category.
     */
    OTHER;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * From string category.
     *
     * @param category the category
     * @return the status
     */
    public static ApiSettingsCategory fromString(String category) {
        return ApiSettingsCategory.valueOf(category.toUpperCase());
    }
}
