package com.eversis.esa.geoss.curated.relations.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * The enum Status.
 */
@Getter
public enum Status {

    /**
     * Draft status.
     */
    DRAFT("draft", 0),
    /**
     * Pending status.
     */
    PENDING("pending", 1),
    /**
     * Approved status.
     */
    APPROVED("approved", 2),
    /**
     * Denied status.
     */
    DENIED("denied", 3),
    /**
     * In trash status.
     */
    IN_TRASH("in-trash", 4);

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
