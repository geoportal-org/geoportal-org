package com.eversis.esa.geoss.curated.resources.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * The enum Status.
 */
@Getter
public enum Status {

    /**
     * Approved status.
     */
    APPROVED("approved", 0),

    /**
     * Pending status.
     */
    PENDING("pending", 1),

    /**
     * Denied status.
     */
    DENIED("denied", 2),

    /**
     * In trash status.
     */
    IN_TRASH("in-trash", 3);

    private final String name;

    private final int value;

    Status(String name, int value) {
        this.name = name;
        this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
        return String.valueOf(name);
    }
}
