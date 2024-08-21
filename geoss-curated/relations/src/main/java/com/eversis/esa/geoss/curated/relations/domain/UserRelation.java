package com.eversis.esa.geoss.curated.relations.domain;

import com.eversis.esa.geoss.curated.common.domain.Status;
import com.eversis.esa.geoss.curated.common.domain.TaskType;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * The type User relation.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_relation")
public class UserRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "userid", nullable = false)
    private String userId;

    @Column(name = "entryname", nullable = false)
    private String entryName;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "tasktype", nullable = false)
    private TaskType taskType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumns({
            @JoinColumn(name = "srcid", referencedColumnName = "srcid"),
            @JoinColumn(name = "srcdatasourceid", referencedColumnName = "srcdatasourceid"),
            @JoinColumn(name = "destid", referencedColumnName = "destid"),
            @JoinColumn(name = "destdatasourceid", referencedColumnName = "destdatasourceid"),
            @JoinColumn(name = "relationtypeid", referencedColumnName = "relationtypeid")
    })
    private EntryRelation entryRelation;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "status", nullable = false)
    private Status status;

    @CreatedDate
    @Column(name = "createddate", nullable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "modifieddate", nullable = false)
    private LocalDateTime modifiedDate;

}
