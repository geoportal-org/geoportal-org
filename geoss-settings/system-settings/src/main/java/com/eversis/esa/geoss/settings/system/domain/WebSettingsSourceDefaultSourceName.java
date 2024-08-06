package com.eversis.esa.geoss.settings.system.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Default source name.
 */
public enum WebSettingsSourceDefaultSourceName implements WebSettingsValue {
    /**
     * Geoss default source name.
     */
    GEOSS("GEOSS"),
    /**
     * Geoss curated default source name.
     */
    GEOSS_CURATED("GEOSSCurated"),
    /**
     * Ameri geoss default source name.
     */
    AMERI_GEOSS("AmeriGEO"),
    /**
     * Next geoss default source name.
     */
    NEXT_GEOSS("NextGEOSS"),
    /**
     * Wikipedia default source name.
     */
    WIKIPEDIA("Wikipedia"),
    /**
     * Zenodo default source name.
     */
    ZENODO("Zenodo");

    private final String value;

    WebSettingsSourceDefaultSourceName(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return value;
    }

    @Override
    public String value() {
        return value;
    }
}
