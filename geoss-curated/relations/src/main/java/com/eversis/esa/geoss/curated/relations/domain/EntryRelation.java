package com.eversis.esa.geoss.curated.relations.domain;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * The type Entry relation.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "entryrelation")
public class EntryRelation {

    @EmbeddedId
    private EntryRelationId id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "srcdatasourceid", insertable = false, updatable = false)
    private RelationDataSources srcDataSource;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destdatasourceid", insertable = false, updatable = false)
    private RelationDataSources destDataSource;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "relationtypeid", insertable = false, updatable = false)
    private RelationType relationType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "srctypeid")
    private Types srcType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "desttypeid")
    private Types destType;

    @Column(name = "iscustom")
    private Integer isCustom = 0;

    @Column(name = "workflowinstanceid", nullable = true)
    private Long workflowInstanceId;

    @Column(name = "deleted", nullable = false)
    private Integer deleted = 0;

    @CreatedDate
    @Column(name = "createddate", nullable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "modifieddate", nullable = false)
    private LocalDateTime modifiedDate;

}
