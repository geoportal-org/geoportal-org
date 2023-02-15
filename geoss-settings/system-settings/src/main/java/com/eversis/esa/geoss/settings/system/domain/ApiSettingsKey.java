package com.eversis.esa.geoss.settings.system.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Api settings name.
 */
public enum ApiSettingsKey {

    /**
     * Google maps api key api settings name.
     */
    GOOGLE_MAPS_API_KEY("googleMapsApiKey"),

    /**
     * W 3 w key api settings name.
     */
    W3W_KEY("w3wKey"),

    /**
     * Dab base url api settings name.
     */
    DAB_BASE_URL("dabBaseUrl"),

    /**
     * Dab data providers url api settings name.
     */
    DAB_DATA_PROVIDERS_URL("dabDataProvidersUrl"),

    /**
     * Dab view api key api settings name.
     */
    DAB_VIEW_API_KEY("dabViewApiKey"),

    /**
     * Dab view base url api settings name.
     */
    DAB_VIEW_BASE_URL("dabViewBaseUrl"),

    /**
     * Kp base url api settings name.
     */
    KP_BASE_URL("kpBaseUrl"),

    /**
     * Kp api key api settings name.
     */
    KP_API_KEY("kpApiKey"),

    /**
     * Next geoss base url api settings name.
     */
    NEXT_GEOSS_BASE_URL("nextGeossBaseUrl");

    private final String value;

    ApiSettingsKey(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return value;
    }

    /**
     * From string name.
     *
     * @param name the name
     * @return the status
     */
    public static ApiSettingsKey fromString(String name) {
        for (ApiSettingsKey apiSettingsName : ApiSettingsKey.values()) {
            if (apiSettingsName.value.equals(name)) {
                return apiSettingsName;
            }
        }
        return ApiSettingsKey.valueOf(name.toUpperCase());
    }
}
