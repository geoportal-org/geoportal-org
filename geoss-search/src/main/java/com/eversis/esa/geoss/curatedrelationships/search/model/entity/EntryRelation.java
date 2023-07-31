package com.eversis.esa.geoss.curatedrelationships.search.model.entity;

import com.eversis.esa.geoss.curatedrelationships.search.model.DataSource;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(of = {"srcDataSource", "srcEntryCode", "destDataSource", "destEntryCode", "relationType"})
@Builder
public class EntryRelation implements Serializable {

    private final DataSource srcDataSource;
    private final String srcEntryCode;
    private final EntryType srcType;
    private final DataSource destDataSource;
    private final String destEntryCode;
    private final EntryType destType;
    private final String relationType;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
