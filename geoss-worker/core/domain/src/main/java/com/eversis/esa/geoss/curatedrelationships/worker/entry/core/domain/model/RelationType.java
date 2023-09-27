package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model;

/**
 * The enum Relation type.
 */
public enum RelationType {
    /**
     * Parent relation type.
     */
    PARENT("parent", "parent");

    private final String name;
    private final String code;

    RelationType(String name, String code) {
        this.name = name;
        this.code = code;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }
}
