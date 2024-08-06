package com.eversis.esa.geoss.settings.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The enum Web settings set.
 */
@Log4j2
@Getter
public enum WebSettingsSet {

    /**
     * The Logo.
     */
    MATOMO(new HashSet<>(EnumSet.allOf(Matomo.class))),

    /**
     * The Source.
     */
    SOURCE(new HashSet<>(EnumSet.allOf(Source.class))),

    /**
     * The Map.
     */
    MAP(new HashSet<>(EnumSet.allOf(Map.class)));

    /**
     * The enum Matomo.
     */
    public enum Matomo implements WebSettingsKey {

        /**
         * Site id matomo.
         */
        SITE_ID("siteId");

        private final String value;

        Matomo(String value) {
            this.value = value;
        }

        @Override
        public String key() {
            return name();
        }

        @Override
        public Set<WebSettingsValue> keyValues() {
            return Collections.emptySet();
        }

        @JsonValue
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * The enum Source.
     */
    public enum Source implements WebSettingsKey {
        /**
         * Default source nome.
         */
        DEFAULT_SOURCE_NAME("defaultSourceName",
                new HashSet<>(EnumSet.allOf(WebSettingsSourceDefaultSourceName.class)));

        private final String value;

        @JsonIgnore
        private final Set<WebSettingsValue> values;

        Source(String value, Set<WebSettingsValue> values) {
            this.value = value;
            this.values = values;
        }

        @Override
        public String key() {
            return name();
        }

        @Override
        public Set<WebSettingsValue> keyValues() {
            return values;
        }

        @JsonValue
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * The enum Map.
     */
    public enum Map implements WebSettingsKey {
        /**
         * Longitude map.
         */
        LONGITUDE,
        /**
         * Latitude map.
         */
        LATITUDE,
        /**
         * Zoom map.
         */
        ZOOM;

        @Override
        public String key() {
            return name();
        }

        @Override
        public Set<WebSettingsValue> keyValues() {
            return Collections.emptySet();
        }

        @JsonValue
        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    @JsonIgnore
    private final Set<WebSettingsKey> keys;

    WebSettingsSet(Set<WebSettingsKey> keys) {
        this.keys = keys;
    }

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }

    /**
     * Gets key.
     *
     * @param key the key
     * @return the key
     */
    public WebSettingsKey getKey(String key) {
        for (WebSettingsKey webSettingskey : keys) {
            if (webSettingskey.key().equals(key) || webSettingskey.toString().equals(key)) {
                return webSettingskey;
            }
        }
        throw new IllegalArgumentException(
                "Invalid key `" + key + "` for set `" + this + "`: not one of the values accepted for " + this
                + " set: " + this.keys);
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
     * Keys list.
     *
     * @return the list
     */
    public static List<String> keys() {
        return EnumSet.allOf(WebSettingsSet.class).stream()
                .flatMap(webSettingsKey -> webSettingsKey.keys.stream())
                .map(WebSettingsKey::toString)
                .toList();
    }

    /**
     * Key from string web settings key.
     *
     * @param key the key
     * @return the web settings key
     */
    public static WebSettingsKey keyFromString(String key) {
        for (WebSettingsSet webSettingsSet : WebSettingsSet.values()) {
            for (WebSettingsKey webSettingsKey : webSettingsSet.keys) {
                if (webSettingsKey.key().equals(key) || webSettingsKey.toString().equals(key)) {
                    return webSettingsKey;
                }
            }
        }
        throw new IllegalArgumentException(
                "Cannot deserialize value of type `" + WebSettingsKey.class.getCanonicalName() + "` from String `" + key
                + "`: not one of the values accepted for " + WebSettingsKey.class.getCanonicalName()
                + " class: " + keys());
    }
}
