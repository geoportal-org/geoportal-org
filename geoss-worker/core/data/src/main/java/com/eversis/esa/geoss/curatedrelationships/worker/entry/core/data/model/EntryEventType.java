package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model;

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

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
