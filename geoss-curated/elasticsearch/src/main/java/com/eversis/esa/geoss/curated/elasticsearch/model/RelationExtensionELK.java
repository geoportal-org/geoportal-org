package com.eversis.esa.geoss.curated.elasticsearch.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Relation extension elk.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelationExtensionELK implements Serializable {

    private String srcEntryCode;

    private String srcDataSource;

    private String srcEntryType;

    private String destEntryCode;

    private String destDataSource;

    private String destEntryType;

    private String relationType;

    private LocalDateTime modifiedDate;

    private LocalDateTime createdDate;

}
