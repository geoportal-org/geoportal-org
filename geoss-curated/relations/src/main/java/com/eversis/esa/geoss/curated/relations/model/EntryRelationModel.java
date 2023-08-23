package com.eversis.esa.geoss.curated.relations.model;

import lombok.Data;
import lombok.NonNull;

/**
 * The type Entry relation model.
 */
@Data
public class EntryRelationModel {

    private final String srcId;
    private final DataSourceModel srcDataSource;
    private final TypeModel srcType;
    private final String destId;
    private final DataSourceModel destDataSource;
    private final TypeModel destType;
    private final RelationTypeModel relationType;

    /**
     * Gets related entry code.
     *
     * @param entryCode the entry code
     * @return the related entry code
     */
    public String getRelatedEntryCode(@NonNull String entryCode) {
        if (srcId.equals(entryCode)) {
            return destId;
        }
        if (destId.equals(entryCode)) {
            return srcId;
        }
        throw new IllegalArgumentException("Provided entry code is not part of this relation");
    }

}
