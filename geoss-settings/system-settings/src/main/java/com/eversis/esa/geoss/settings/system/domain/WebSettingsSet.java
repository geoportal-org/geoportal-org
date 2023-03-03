package com.eversis.esa.geoss.settings.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.EnumSet;
import java.util.List;

/**
 * The enum Web settings set.
 */
@Log4j2
@Getter
public enum WebSettingsSet {

    /**
     * Logo web settings set.
     */
    LOGO(EnumSet.allOf(WebSettingsLogo.class)),

    /**
     * Source web settings set.
     */
    SOURCE(EnumSet.allOf(WebSettingsSource.class)),

    /**
     * Map web settings set.
     */
    MAP(EnumSet.allOf(WebSettingsMap.class));

    /**
     * The enum Web settings logo.
     */
    enum WebSettingsLogo {
        /**
         * Title web settings logo.
         */
        TITLE,
        /**
         * Source web settings logo.
         */
        SOURCE;

        @JsonValue
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    /**
     * The enum Web settings source.
     */
    enum WebSettingsSource {
        /**
         * Geoss web settings source.
         */
        GEOSS("GEOSS"),
        /**
         * The Geoss curated.
         */
        GEOSS_CURATED("GEOSSCurated"),
        /**
         * Ameri geoss web settings source.
         */
        AMERI_GEOSS("AmeriGEO"),
        /**
         * Next geoss web settings source.
         */
        NEXT_GEOSS("NextGEOSS"),
        /**
         * Wikipedia web settings source.
         */
        WIKIPEDIA(
                "Wikipedia"),
        /**
         * Zenodo web settings source.
         */
        ZENODO("Zenodo");
        private final String value;

        WebSettingsSource(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * The enum Web settings map.
     */
    enum WebSettingsMap {
        /**
         * Longitude web settings map.
         */
        LONGITUDE,
        /**
         * Latitude web settings map.
         */
        LATITUDE,
        /**
         * Zoom web settings map.
         */
        ZOOM;

        @JsonValue
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    @JsonIgnore
    private final EnumSet<?> keys;

    WebSettingsSet(EnumSet<?> keys) {
        this.keys = keys;
    }

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * From string web settings set.
     *
     * @param category the category
     * @return the web settings set
     */
    public static WebSettingsSet fromString(String category) {
        return WebSettingsSet.valueOf(category.toUpperCase());
    }

    /**
     * Valid key.
     *
     * @param key the key
     */
    public void validKey(String key) {
        for (Enum<?> k : keys) {
            if (k.name().equals(key.toUpperCase())) {
                return;
            }
        }
        throw new IllegalArgumentException(
                "Invalid key " + key + " for set " + this + ": not one of the values accepted for " + this + ": "
                        + this.keys);
    }

    /**
     * Keys string [ ].
     *
     * @return the string [ ]
     */
    public static List<String> keys() {
        return EnumSet.allOf(WebSettingsSet.class).stream()
                .flatMap(webSettingsSet -> webSettingsSet.keys.stream())
                .map(Enum::toString)
                .toList();
    }
}
