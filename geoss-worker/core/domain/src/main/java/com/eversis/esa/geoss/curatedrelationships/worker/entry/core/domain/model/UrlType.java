package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model;

/**
 * The enum Url type.
 */
public enum UrlType {

    /**
     * Http url type.
     */
    HTTP("HTTP"),
    /**
     * Access type simple url type.
     */
    ACCESS_TYPE_SIMPLE("http://www.essi-lab.eu/broker/accesstypes/simple"),
    /**
     * Access type complex url type.
     */
    ACCESS_TYPE_COMPLEX("http://www.essi-lab.eu/broker/accesstypes/complex"),
    /**
     * Kml url type.
     */
    KML("KML");

    private String value;

    UrlType(String value) {
        this.value = value;
    }

    /**
     * Gets value.
     *
     * @return the value
     */
    public String getValue() {
        return value;
    }
}
