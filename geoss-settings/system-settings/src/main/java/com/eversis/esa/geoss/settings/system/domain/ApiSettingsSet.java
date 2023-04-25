package com.eversis.esa.geoss.settings.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The enum Api settings set.
 */
@Getter
public enum ApiSettingsSet {

    /**
     * The External.
     */
    EXTERNAL(new HashSet<>(EnumSet.allOf(External.class))),

    /**
     * The Dab.
     */
    DAB(new HashSet<>(EnumSet.allOf(DAB.class))),

    /**
     * The Knowledge producer.
     */
    KNOWLEDGE_PRODUCER(new HashSet<>(EnumSet.allOf(KnowledgeProducer.class))),

    // CURATED(new HashSet<>(EnumSet.allOf(Curated.class))),

    /**
     * The Other.
     */
    OTHER(new HashSet<>(EnumSet.allOf(Other.class)));

    /**
     * The enum External.
     */
    enum External implements ApiSettingsKey {

        /**
         * Google maps api key external.
         */
        EXT_GOOGLE_MAPS_API_KEY("googleMapsApiKey"),

        /**
         * W 3 w key external.
         */
        EXT_W3W_KEY("w3wKey");

        private final String value;

        External(String value) {
            this.value = value;
        }

        @Override
        public String key() {
            return name();
        }

        @JsonValue
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * The enum Dab.
     */
    enum DAB implements ApiSettingsKey {

        /**
         * Base url dab.
         */
        DAB_BASE_URL("dabBaseUrl"),

        /**
         * Data providers url dab.
         */
        DAB_DATA_PROVIDERS_URL("dabDataProvidersUrl"),

        /**
         * View api key dab.
         */
        DAB_VIEW_API_KEY("dabViewApiKey"),

        /**
         * View base url dab.
         */
        DAB_VIEW_BASE_URL("dabViewBaseUrl");

        private final String value;

        DAB(String value) {
            this.value = value;
        }

        @Override
        public String key() {
            return name();
        }

        @JsonValue
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * The enum Knowledge producer.
     */
    enum KnowledgeProducer implements ApiSettingsKey {

        /**
         * Base url knowledge producer.
         */
        KP_BASE_URL("kpBaseUrl"),

        /**
         * Api key knowledge producer.
         */
        KP_API_KEY("kpApiKey");

        private final String value;

        KnowledgeProducer(String value) {
            this.value = value;
        }

        @Override
        public String key() {
            return name();
        }

        @JsonValue
        @Override
        public String toString() {
            return value;
        }
    }

    /**
     * The enum Other.
     */
    enum Other implements ApiSettingsKey {

        /**
         * Next geoss base url other.
         */
        NEXT_GEOSS_BASE_URL("nextGeossBaseUrl");

        private final String value;

        Other(String value) {
            this.value = value;
        }

        @Override
        public String key() {
            return name();
        }

        @JsonValue
        @Override
        public String toString() {
            return value;
        }
    }

    @JsonIgnore
    private final Set<ApiSettingsKey> keys;

    ApiSettingsSet(Set<ApiSettingsKey> keys) {
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
    public ApiSettingsKey getKey(String key) {
        for (ApiSettingsKey apiSettingsKey : keys) {
            if (apiSettingsKey.key().equals(key) || apiSettingsKey.toString().equals(key)) {
                return apiSettingsKey;
            }
        }
        throw new IllegalArgumentException(
                "Invalid key `" + key + "` for set `" + this + "`: not one of the values accepted for " + this
                        + " set: " + this.keys);
    }

    /**
     * From string api settings set.
     *
     * @param category the category
     * @return the api settings set
     */
    public static ApiSettingsSet fromString(String category) {
        return ApiSettingsSet.valueOf(category.toUpperCase());
    }

    /**
     * Keys list.
     *
     * @return the list
     */
    public static List<String> keys() {
        return EnumSet.allOf(ApiSettingsSet.class).stream()
                .flatMap(apiSettingsSet -> apiSettingsSet.keys.stream())
                .map(ApiSettingsKey::toString)
                .toList();
    }

    /**
     * Key from string api settings key.
     *
     * @param key the key
     * @return the api settings key
     */
    public static ApiSettingsKey keyFromString(String key) {
        for (ApiSettingsSet apiSettingsSet : ApiSettingsSet.values()) {
            for (ApiSettingsKey apiSettingsKey : apiSettingsSet.keys) {
                if (apiSettingsKey.key().equals(key) || apiSettingsKey.toString().equals(key)) {
                    return apiSettingsKey;
                }
            }
        }
        throw new IllegalArgumentException(
                "Cannot deserialize value of type `" + ApiSettingsKey.class.getCanonicalName() + "` from String `" + key
                        + "`: not one of the values accepted for " + ApiSettingsKey.class.getCanonicalName()
                        + " class: " + keys());
    }
}
