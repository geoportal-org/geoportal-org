package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model;

/**
 * The enum Definition type.
 */
public enum DefinitionType {

    /**
     * Predefined definition type.
     */
    PREDEFINED(0, "Predefined"),
    /**
     * The User defined.
     */
    USER_DEFINED(1, "User defined"),
    /**
     * The External resource copy.
     */
    EXTERNAL_RESOURCE_COPY(2, "External resource copy");

    private final int code;
    private final String name;

    DefinitionType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Gets by code.
     *
     * @param code the code
     * @return the by code
     */
    public static DefinitionType getByCode(int code) {
        for (DefinitionType definitionType : DefinitionType.values()) {
            if (definitionType.getCode() == code) {
                return definitionType;
            }
        }
        throw new IllegalArgumentException("Provided definitionType code: " + code + " is not recognized");
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
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
