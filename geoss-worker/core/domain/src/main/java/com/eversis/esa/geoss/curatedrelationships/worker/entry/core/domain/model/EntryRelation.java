package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.domain.model;

import lombok.Data;
import lombok.NonNull;

/**
 * The type Entry relation.
 */
@Data
public class EntryRelation {

    private final String sourceEntryCode;
    private final DataSource sourceDataSource;
    private final Type sourceType;
    private final String destinationEntryCode;
    private final DataSource destinationDataSource;
    private final Type destinationType;
    private final RelationType relationType;

    /**
     * Gets related entry code.
     *
     * @param entryCode the entry code
     * @return the related entry code
     */
    public String getRelatedEntryCode(@NonNull String entryCode) {
        if (sourceEntryCode.equals(entryCode)) {
            return destinationEntryCode;
        }
        if (destinationEntryCode.equals(entryCode)) {
            return sourceEntryCode;
        }
        throw new IllegalArgumentException("Provided entry code is not part of this relation");
    }
}
