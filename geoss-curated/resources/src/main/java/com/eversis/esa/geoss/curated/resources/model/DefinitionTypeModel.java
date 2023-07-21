package com.eversis.esa.geoss.curated.resources.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Definition type model.
 */
public enum DefinitionTypeModel {

    /**
     * Predefined definition type model.
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

    DefinitionTypeModel(int code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Gets by code.
     *
     * @param code the code
     * @return the by code
     */
    public static DefinitionTypeModel getByCode(int code) {
        for (DefinitionTypeModel definitionType : DefinitionTypeModel.values()) {
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

    @JsonValue
    @Override
    public String toString() {
        return String.valueOf(code);
    }

}
