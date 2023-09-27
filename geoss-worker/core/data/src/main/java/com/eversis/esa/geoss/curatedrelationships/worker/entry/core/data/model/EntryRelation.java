package com.eversis.esa.geoss.curatedrelationships.worker.entry.core.data.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The type Entry relation.
 */
@Getter
@Setter
@Entity
@Table(name = "entryrelation")
public class EntryRelation extends Auditable {

    @EmbeddedId
    private EntryRelationId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "srcdatasourceid", insertable = false, updatable = false)
    private DataSource srcDataSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destdatasourceid", insertable = false, updatable = false)
    private DataSource destDataSource;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relationtypeid", insertable = false, updatable = false)
    private RelationType relationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "srctypeid")
    private Type srcType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "desttypeid")
    private Type destType;

    @Column(name = "iscustom")
    private Integer isCustom = 0;

}
