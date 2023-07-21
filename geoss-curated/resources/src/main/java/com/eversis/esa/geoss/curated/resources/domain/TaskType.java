package com.eversis.esa.geoss.curated.resources.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * The enum Task type.
 */
@Getter
public enum TaskType {

    /**
     * Create task type.
     */
    CREATE("create", 0),

    /**
     * Update task type.
     */
    UPDATE("update", 1),

    /**
     * Remove task type.
     */
    REMOVE("remove", 2);

    private final String name;

    private final int value;

    TaskType(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return String.valueOf(name);
    }

}
