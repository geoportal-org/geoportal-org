package com.eversis.esa.geoss.curated.relations.model;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Relation type model.
 */
public enum RelationTypeModel {

    /**
     * Parent relation type model.
     */
    PARENT("Parent", "Parent")
    ;

    private final String name;
    private final String code;

    RelationTypeModel(String name, String code) {
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

    @JsonValue
    @Override
    public String toString() {
        return String.valueOf(code);
    }

}
