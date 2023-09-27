package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model;

/**
 * The enum Access policy.
 */
public enum AccessPolicy {
    /**
     * Open access policy.
     */
    OPEN("Open", "open");

    private final String name;
    private final String value;

    AccessPolicy(String name, String value) {
        this.name = name;
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

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
}
