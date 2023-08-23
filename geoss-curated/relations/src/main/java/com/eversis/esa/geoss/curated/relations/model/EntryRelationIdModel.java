package com.eversis.esa.geoss.curated.relations.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Entry relation id model.
 */
@Data
@AllArgsConstructor
public class EntryRelationIdModel {

    private String srcId;

    private int srcDataSourceId;

    private String destId;

    private int destDataSourceId;

    private int relationTypeId;

}
