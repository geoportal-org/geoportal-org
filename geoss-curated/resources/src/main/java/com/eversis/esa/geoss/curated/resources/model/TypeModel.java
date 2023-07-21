package com.eversis.esa.geoss.curated.resources.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Type model.
 */
public enum TypeModel {

    /**
     * Service type model.
     */
    SERVICE("service_resource", "Service"),

    /**
     * Data type model.
     */
    DATA("data_resource", "Data"),

    /**
     * Information type model.
     */
    INFORMATION("information_resource", "Information"),

    /**
     * Download type model.
     */
    DOWNLOAD("download_resource", "Download");

    private final String code;
    private final String name;

    TypeModel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    @JsonValue
    @Override
    public String toString() {
        return String.valueOf(code);
    }

}
