package com.eversis.esa.geoss.curated.resources.domain;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The enum Entry event type.
 */
public enum EntryEventType {
    /**
     * Create entry event type.
     */
    CREATE,
    /**
     * Update entry event type.
     */
    UPDATE,
    /**
     * Delete entry event type.
     */
    DELETE;

    @JsonValue
    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
