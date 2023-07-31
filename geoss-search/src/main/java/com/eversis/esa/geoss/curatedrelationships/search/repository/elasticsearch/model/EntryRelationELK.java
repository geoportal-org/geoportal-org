package com.eversis.esa.geoss.curatedrelationships.search.repository.elasticsearch.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * The type Entry relation elk.
 */
@Data
public class EntryRelationELK implements Serializable {

    private String srcDataSource;
    private String srcEntryCode;
    private String srcEntryType;
    private String destDataSource;
    private String destEntryCode;
    private String destEntryType;
    private String relationType;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
