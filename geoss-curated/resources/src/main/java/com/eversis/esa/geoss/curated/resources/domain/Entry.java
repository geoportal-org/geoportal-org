package com.eversis.esa.geoss.curated.resources.domain;

import com.eversis.esa.geoss.curated.common.domain.DataSource;
import com.eversis.esa.geoss.curated.common.domain.Type;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.NaturalId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

/**
 * The type Entry.
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "entry")
public class Entry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "title", nullable = true)
    private String title;

    @Column(name = "summary", columnDefinition = "TEXT", nullable = true)
    private String summary;

    @Column(name = "logo", nullable = true)
    private String logo;

    @Column(name = "coverage", nullable = true)
    private String coverage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "typeid")
    private Type type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "dashboardcontentsid")
    private DashboardContents dashboardContents;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "accesspolicyid")
    private AccessPolicy accessPolicy;

    @Column(name = "keywords", nullable = true)
    private String keywords;

    @Column(name = "tags", nullable = true)
    private String tags;

    @NaturalId
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "scoreweight", nullable = true)
    private Double scoreWeight = 1.0;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "organisationid")
    private Organisation organisation;

    @Column(name = "publishdate", nullable = true)
    private LocalDateTime publishDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sourceid")
    private Source source;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "datasourceid")
    private DataSource dataSource;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "displaydatasourceid")
    private DataSource displayDataSource;

    @Column(name = "workflowinstanceid", nullable = true)
    private Long workflowInstanceId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "definitiontypeid")
    private DefinitionType definitionType;

    @Column(name = "deleted", nullable = false)
    private Integer deleted = 0;

    @CreatedDate
    @Column(name = "createddate", nullable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "modifieddate", nullable = false)
    private LocalDateTime modifiedDate;

}
