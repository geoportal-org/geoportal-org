package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model;

/**
 * The enum Type.
 */
public enum Type {

    /**
     * Service type.
     */
    SERVICE("service_resource", "Service"),
    /**
     * Data type.
     */
    DATA("data_resource", "Data"),
    /**
     * Information type.
     */
    INFORMATION("information_resource", "Information"),
    /**
     * Download type.
     */
    DOWNLOAD("download_resource", "Download");

    private final String code;
    private final String name;

    Type(String code, String name) {
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
}
