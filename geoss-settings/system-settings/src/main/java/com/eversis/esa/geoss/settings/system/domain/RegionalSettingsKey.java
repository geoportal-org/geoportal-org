package com.eversis.esa.geoss.settings.system.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Regional settings key.
 */
public enum RegionalSettingsKey {

    /**
     * Current regional settings key.
     */
    CURRENT;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * From string regional settings key.
     *
     * @param id the id
     * @return the regional settings key
     */
    public static RegionalSettingsKey fromString(String id) {
        return RegionalSettingsKey.valueOf(id.toUpperCase());
    }
}
