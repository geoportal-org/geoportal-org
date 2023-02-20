package com.eversis.esa.geoss.settings.instance.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Tag placement.
 */
public enum TagPlacement {

    /**
     * Left top tag placement.
     */
    LEFT_TOP("left-top"),

    /**
     * Left bottom tag placement.
     */
    LEFT_BOTTOM("left-bottom"),

    /**
     * Left center tag placement.
     */
    LEFT_CENTER("left-center"),

    /**
     * Right top tag placement.
     */
    RIGHT_TOP("right-top"),

    /**
     * Right bottom tag placement.
     */
    RIGHT_BOTTOM("right-bottom"),

    /**
     * Right center tag placement.
     */
    RIGHT_CENTER("right-center"),

    /**
     * Top center tag placement.
     */
    TOP_CENTER("top-center"),

    /**
     * Bottom center tag placement.
     */
    BOTTOM_CENTER("bottom-center"),

    /**
     * Center center tag placement.
     */
    CENTER_CENTER("center-center");

    private final String value;

    TagPlacement(String value) {
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return value;
    }

    /**
     * From string tag placement.
     *
     * @param name the name
     * @return the tag placement
     */
    public static TagPlacement fromString(String name) {
        for (TagPlacement apiSettingsName : TagPlacement.values()) {
            if (apiSettingsName.value.equals(name)) {
                return apiSettingsName;
            }
        }
        return TagPlacement.valueOf(name.toUpperCase());
    }
}
